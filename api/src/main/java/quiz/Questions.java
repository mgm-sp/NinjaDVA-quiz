package quiz;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Questions {
	List<Question> questions = new ArrayList<Question>();

	@XmlElementWrapper(name="questions")
	@XmlElement(name="question")
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public void addQuestion(Question q) {
		questions.add(q);
	}

}
