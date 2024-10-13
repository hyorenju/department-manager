package vn.edu.vnua.department.service.mail;

import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.user.entity.User;

public interface MailService {
    void sendExamWarningMail(Exam exam, String deadline);
    void sendMemberJoinTaskMail(Task task, User user);
}
