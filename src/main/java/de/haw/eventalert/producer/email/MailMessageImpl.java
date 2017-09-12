package de.haw.eventalert.producer.email;

/**
 * Created by Tim on 18.08.2017.
 */
public class MailMessageImpl implements MailMessage {

    private Long imapAccountId;
    private String from;
    private String to;
    private String replyTo;
    private String cc;
    private String bcc;
    private Long sendTime;
    private Long receivedTime;
    private String subject;
    private String content;

    public void setImapAccountId(Long imapAccountId) {
        this.imapAccountId = imapAccountId;
    }

    void setFrom(String from) {
        this.from = from;
    }

    void setTo(String to) {
        this.to = to;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    void setReceivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Long getImapAccountId() {
        return imapAccountId;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getReplyTo() {
        return replyTo;
    }

    @Override
    public String getCc() {
        return cc;
    }

    @Override
    public String getBcc() {
        return bcc;
    }

    @Override
    public Long getSendTime() {
        return sendTime;
    }

    @Override
    public Long getReceivedTime() {
        return receivedTime;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "MailMessageImpl{" +
                "imapAccountId=" + imapAccountId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", replyTo='" + replyTo + '\'' +
                ", cc='" + cc + '\'' +
                ", bcc='" + bcc + '\'' +
                ", sendTime=" + sendTime +
                ", receivedTime=" + receivedTime +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
