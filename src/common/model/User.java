package common.model;

/**
 * User is the data structure of a row in users table in the database
 *
 * @author Daohan Chong, Dimitrios Simopoulos, Mel Trout, Yang He
 * @version 2018-02-24
 */
public class User {

    private int uid;
    private String username;
    private String password;
    private int mark;
    private boolean isTeacher;

    /**
     * Get the user id
     *
     * @return the user id
     */
    public int getUid() {
        return uid;
    }

    /**
     * Set the user id
     *
     * @param uid is the new user id
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Get the username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     *
     * @param username is the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     *
     * @param password is the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the mark
     *
     * @return the mark of the user
     */
    public int getMark() {
        return mark;
    }

    /**
     * Set the mark
     *
     * @param mark is the new mark
     */
    public void setMark(int mark) {
        this.mark = mark;
    }

    /**
     * Get the boolean value indicating whether the user is a frontend.teacher
     *
     * @return whether the user is a frontend.teacher
     */
    public boolean getTeacher() {
        return isTeacher;
    }

    /**
     * Set the boolean value indicating whether the user is a frontend.teacher
     *
     * @param teacher is the boolean value indicating whether the user is a frontend.teacher
     */
    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    /**
     * @return the String representation for this User instance
     */
    @Override
    public String toString() {
        return "[User uid: " + uid + ", username: " + username + ", mark: " + mark + ", is_teacher: "
                + isTeacher + "]";
    }

    /**
     * Compare whether the object refers to the same user info
     *
     * @param obj is the object to be compared
     * @return true if they have the same uid or the uids are all zero and the other info excluding password is identical
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User toBeCompared = (User) obj;
            if (getUid() > 0 && toBeCompared.getUid() == getUid()) return true;
            else if (getUid() == 0 && toBeCompared.getUid() == 0
                    && getUsername().equals(toBeCompared.getUsername())
                    && getMark() == toBeCompared.getMark()
                    && getTeacher() == toBeCompared.getTeacher()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
