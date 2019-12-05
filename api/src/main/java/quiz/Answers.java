
package quiz;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Answers {

//	@XmlElementWrapper(name="answers")
	@XmlElement(name="answer")
	List<String> answers = new ArrayList<String>();

	public List<String> getAnswers() {
		return answers;
	}
	public String getAnswer(int i) {
		return answers.get(i);
	}



}
