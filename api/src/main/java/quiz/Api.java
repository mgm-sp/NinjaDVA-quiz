package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@WebService
public class Api {
	Connection conn;
	public Api() throws Exception {
		conn = DriverManager.getConnection("jdbc:mysql://quiz-db:3306/quiz?serverTimezone=UTC", "database_user", "damn_secret_password") ;
	}
	private Connection get_Connection() throws SQLException {
		if (conn.isClosed()) {
			conn = DriverManager.getConnection("jdbc:mysql://quiz-db:3306/quiz?serverTimezone=UTC", "database_user", "damn_secret_password") ;
		}
		return conn;
	}

	@WebMethod
	public Questions fetchQuestions() throws Exception {
		ResultSet rs;
		rs = get_Connection().createStatement().executeQuery("SELECT question_round, total_questions FROM question_rounds WHERE current_round = true");
		rs.next();
		int currentRound = rs.getInt("question_round");

		PreparedStatement stmt = get_Connection().prepareStatement("SELECT question_number, title, type FROM questions WHERE question_round = ? ORDER BY question_number");
		stmt.setInt(1, currentRound);
		rs = stmt.executeQuery();

		Questions qs = new Questions();
		while ( rs.next() ) {
			Question q = new Question();
			q.setTitle(rs.getString("title"));
			q.setType(rs.getString("type"));
			PreparedStatement answerStatement = get_Connection().prepareStatement("SELECT answer_number, text FROM possible_answers WHERE question_round = ? AND question_number = ? ORDER BY answer_number");
			answerStatement.setInt(1, currentRound);
			answerStatement.setInt(2, rs.getInt("question_number"));
			ResultSet answersQuery = answerStatement.executeQuery();
			while ( answersQuery.next() ) {
				q.addAnswer(answersQuery.getString("text"));
			}
			qs.addQuestion(q);
		}


		return qs;
	}


	@WebMethod
	public Question fetchQuestion(@WebParam(name="questionNumber")int questionNumber) throws Exception {
		ResultSet rs;
		rs = get_Connection().createStatement().executeQuery("SELECT question_round,total_questions FROM question_rounds WHERE current_round = true");
		rs.next();
		int currentRound = rs.getInt("question_round");
		int totalQuestions = rs.getInt("total_questions");

		if (0 <= questionNumber && questionNumber < totalQuestions) {
			PreparedStatement stmt = get_Connection().prepareStatement("SELECT title, type FROM questions WHERE question_round = ? AND question_number = ?");
			stmt.setInt(1, currentRound);
			stmt.setInt(2, questionNumber);
			rs = stmt.executeQuery();

			if (rs.next()){
				Question q = new Question();
				q.setTitle(rs.getString("title"));
				q.setType(rs.getString("type"));
				PreparedStatement answerStatement = get_Connection().prepareStatement("SELECT answer_number, text FROM possible_answers WHERE question_round = ? AND question_number = ? ORDER BY answer_number");
				answerStatement.setInt(1, currentRound);
				answerStatement.setInt(2, questionNumber);
				ResultSet answersQuery = answerStatement.executeQuery();
				while ( answersQuery.next() ) {
					q.addAnswer(answersQuery.getString("text"));
				}
				return q;
			}
		}
		return null;
	}
	@WebMethod
	public int totalQuestions() throws Exception {
		ResultSet rs;
		rs = get_Connection().createStatement().executeQuery("SELECT total_questions FROM question_rounds WHERE current_round = true");
		rs.next();
		return rs.getInt("total_questions");
	}

	@WebMethod
	public String store(@WebParam(name="studentId")String studentId, @WebParam(name="answers")List<Answers> answerlist) {
		String retval = "";
		try {
			ResultSet rs;
			rs = get_Connection().createStatement().executeQuery("SELECT question_round FROM question_rounds WHERE current_round = true");
			rs.next();
			int currentRound = rs.getInt("question_round");

			rs = get_Connection().createStatement().executeQuery("SELECT total_questions FROM question_rounds WHERE current_round = true");
			rs.next();
			for (int question_number = 0; question_number < answerlist.size(); question_number++) {
				List<String> answers = answerlist.get(question_number).getAnswers();
				for (int answer_number = 0; answer_number < answers.size(); answer_number++) {
					String answer = answers.get(answer_number);

					// check if entry is in database
					PreparedStatement preparedStmt = get_Connection().prepareStatement(
							"SELECT question_number "
							+ "FROM answers "
							+ "WHERE student = ? AND question_round = ? AND question_number = ? AND answer_number = ?");
					preparedStmt.setString(1, studentId);
					preparedStmt.setInt(2, currentRound);
					preparedStmt.setInt(3, question_number);
					preparedStmt.setInt(4, answer_number);
					ResultSet r = preparedStmt.executeQuery();

					if(r.first()){
						// UPDATE
						preparedStmt = get_Connection().prepareStatement(
								"UPDATE answers "
								+ "SET text = '"+ answer +"' "
								+ "WHERE student = ? AND question_round = ? AND question_number = ? AND answer_number = ? ");
						preparedStmt.setString(1, studentId);
						preparedStmt.setInt(2, currentRound);
						preparedStmt.setInt(3, question_number);
						preparedStmt.setInt(4, answer_number);
						preparedStmt.executeUpdate();
						retval = "The answers were updated successfully";
					} else {
						preparedStmt = get_Connection().prepareStatement(
								"INSERT INTO answers (student,question_round,question_number,answer_number,text) "
								+ "VALUES (?,?,?,?,'" + answer + "')");
						preparedStmt.setString(1, studentId);
						preparedStmt.setInt(2, currentRound);
						preparedStmt.setInt(3, question_number);
						preparedStmt.setInt(4, answer_number);
						preparedStmt.executeUpdate();
						retval = "The answers were stored successfully";
					}
				}
			}
		} catch (SQLException e) {
			//retval = e.getMessage();
		}
		return retval;
	}
}
