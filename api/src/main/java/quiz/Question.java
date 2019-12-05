package quiz;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Question {
	String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer(int i) {
		return this.answers.get(i);
	}
    public List<String> getAnswers() {
		return answers;
	}
	public void addAnswer(String answer) {
		answers.add(answer);
	}
	@XmlElementWrapper(name="answers")
	@XmlElement(name="answer")
	 List<String> answers = new ArrayList<String>();
}
