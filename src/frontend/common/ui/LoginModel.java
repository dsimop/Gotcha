package frontend.common.ui;

import common.Message;
import common.Utils;
import common.model.User;
import frontend.student.model.StudentClientRootModel;

import java.io.IOException;
import java.util.Observable;

import static frontend.GotchaClientApp.studentClientCommunicationThread;

/**
 * The LoginModel class acts as a DSO for the login panels. It retrieves
 * information from the user and sends it to the CommunicationThreadLogin to be
 * send to the sever.
 *
 * @author Melisha Trout, Dimitrios Simopoulos, Daohan Chong
 * @version 2018-03-17
 */
public class LoginModel extends Observable {
	protected boolean isStudent = false;
	private User currentUser;

	public int getCurrentUserID() {
		return (currentUser == null) ? -1 : currentUser.getUid();
	}

	public void broadcastLoginFailed() {
		setChanged();
		notifyObservers(false);
	}

	private StudentClientRootModel getStudentClientRootModel() {
		return StudentClientRootModel.getSingletonInstance();
	}

	public void broadcastLoginSucceed() {
		setChanged();
		notifyObservers(true);
	}

	public void sendUserDetailToSever(String userName, String password) throws IOException {
		System.out.println("details passed to model");
		User user = new User();
		user.setUsername(userName.trim());
		user.setPassword(password);
		System.out.println(user.getUsername() + " " + user.getPassword());
		studentClientCommunicationThread.sendUsersDetails2Server(user);
	}

	public void sendNewUsersDetails(String userName, String password, Boolean isTeacher) {
		System.out.println("details passed to model");
		User user = new User();
		user.setUsername(userName);
		user.setPassword(password);
		user.setTeacher(isTeacher);
		System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getTeacher());
		studentClientCommunicationThread.sendNewUser2Server(user);
	}

	/**
	 * The whichClient method notify the view panel which client has logged in
	 *
	 * @param user
	 *            User object of the client that just logged in.
	 */
	public boolean whichClient(Message<User> user) {
		if (user.getOperation().equals(Message.ClientOperations.LOGIN_SUCCEED_TEACHER)) {
			isStudent = false;
			// make the relevant changes for the frontend.teacher client
			return true;
		} else if (user.getOperation().equals(Message.ClientOperations.LOGIN_SUCCEED_STUDENT)) {
			isStudent = true;
			// Send the user object to the Student home page
			getStudentClientRootModel().getStudentHomeModel().getCurrentUserDetials(user);
			getStudentClientRootModel().getStudentHomeModel().updateStudentHomeName();

			return true;
		} else {
			return false;
		}

	}

	public String convertPassword(char[] password) {
		StringBuffer newPas = new StringBuffer();
		for (char a : password) {
			newPas.append(a);
		}
		return newPas.toString();
	}

	public void setCurrentUser(User currentUser) {
		Utils.logInfo("Set user:", currentUser);
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
