package apps.picart.Objects;

/**
 * Created by ilyada on 14.04.2018.
 */

public class Comments {

    String commenter_mail;
    String commenter_name;
    String comment_desc;


//    COMMENTS LE UGRAS!
    public String getCommenter_mail() {
        return commenter_mail;
    }

    public void setCommenter_mail(String commenter_mail) {
        this.commenter_mail = commenter_mail;
    }

    public String getCommenter_name() {
        return commenter_name;
    }

    public void setCommenter_name(String commenter_name) {
        this.commenter_name = commenter_name;
    }

    public String getComment_desc() {
        return comment_desc;
    }

    public void setComment_desc(String comment_desc) {
        this.comment_desc = comment_desc;
    }
}
