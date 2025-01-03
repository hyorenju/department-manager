package vn.edu.vnua.department.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.util.MyUtils;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendExamWarningMail(Exam exam, String deadline) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Bạn có cảnh báo nộp điểm";
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body {" +
                "    font-family: Arial, sans-serif;" +
                "    margin: 0;" +
                "    padding: 0;" +
                "}" +
                ".container {" +
                "    max-width: 500px;" +
                "    margin: 0 auto;" +
                "    justify-content: center;" +
                "    align-items: center;" +
                "}" +
                ".title {" +
                "    text-align: center;" +
                "    margin: 0 auto;" +
                "}" +
                ".button {" +
                "    background-color: #00984F;" +
                "    border: none;" +
                "    color: white;" +
                "    padding: 15px 32px;" +
                "    text-align: center;" +
                "    text-decoration: none;" +
                "    display: inline-block;" +
                "    font-size: 16px;" +
                "    margin: 4px 2px;" +
                "    cursor: pointer;" +
                "    border-radius: 8px;" +
                "    transition: background-color 0.3s;" +
                "}" +
                ".button:hover {" +
                "    background-color: #00532B;" +
                "}" +
                "h1 {" +
                "    color: #333;" +
                "}" +
                "p {" +
                "    color: #555;" +
                "    margin: 0 20px;" +
                "    font-size: 15px;" +
                "}" +
                "hr {" +
                "    margin: 0 10px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='title'>" +
                "<h1>CẢNH BÁO HẠN NỘP ĐIỂM</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p style=\"font-weight: bold;\">Thầy/cô chú ý hạn nộp điểm " + deadline.toUpperCase() + " cho nhóm thi có thông tin như sau:</p>" +
                "<p>Mã môn thi: " + exam.getSubject().getId() + "</p>" +
                "<p>Tên môn thi: " + exam.getSubject().getName() + "</p>" +
                "<p>Nhóm: " + exam.getExamGroup() + " - Tổ: " + exam.getCluster() + "</p>" +
                "<p>Học kỳ " + exam.getTerm() + " - Năm học: " + exam.getSchoolYear().getName() + "</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            helper.setTo(exam.getLecturerTeach().getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi email: " + e.getMessage());
        }
    }

    @Override
    public void sendMemberJoinTaskMail(Task task, User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Bạn có công việc mới";
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body {" +
                "    font-family: Arial, sans-serif;" +
                "    margin: 0;" +
                "    padding: 0;" +
                "}" +
                ".container {" +
                "    max-width: 500px;" +
                "    margin: 0 auto;" +
                "    justify-content: center;" +
                "    align-items: center;" +
                "}" +
                ".title {" +
                "    text-align: center;" +
                "    margin: 0 auto;" +
                "}" +
                ".button {" +
                "    background-color: #00984F;" +
                "    border: none;" +
                "    color: white;" +
                "    padding: 15px 32px;" +
                "    text-align: center;" +
                "    text-decoration: none;" +
                "    display: inline-block;" +
                "    font-size: 16px;" +
                "    margin: 4px 2px;" +
                "    cursor: pointer;" +
                "    border-radius: 8px;" +
                "    transition: background-color 0.3s;" +
                "}" +
                ".button:hover {" +
                "    background-color: #00532B;" +
                "}" +
                "h1 {" +
                "    color: #333;" +
                "}" +
                "p {" +
                "    color: #555;" +
                "    margin: 0 20px;" +
                "    font-size: 15px;" +
                "}" +
                "hr {" +
                "    margin: 0 10px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='title'>" +
                "<h1>Bạn có công việc mới</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Công việc mới \"" + task.getName() + "\" có thông tin như sau:</p>" +
                "<p>Mô tả: " + (task.getDescription() != null ? task.getDescription() : "[trống]") + "</p>" +
                "<p>Thời gian thực hiện: " + MyUtils.convertTimestampToString(task.getStart()) + " - " + MyUtils.convertTimestampToString(task.getDeadline()) + "</p>" +
                "<p>Được tạo bởi: " + task.getProject().getCreatedBy().getFirstName() + " " + task.getProject().getCreatedBy().getLastName() + "</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi email: " + e.getMessage());
        }

    }
}
