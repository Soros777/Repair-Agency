package ua.epam.finalproject.repairagency.customTag;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.User;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class UserNameTag extends TagSupport {

    private User user;
    private static final Logger Log = Logger.getLogger(UserNameTag.class);

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        if(user != null) {
            try {
                JspWriter out = pageContext.getOut();
                out.write(user.getPersonName());
            } catch (IOException e) {
                Log.error("Can't write in the out cause : " + e);
                throw new AppException("Internal server error");
            }
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
