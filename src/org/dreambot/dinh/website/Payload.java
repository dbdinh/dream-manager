package org.dreambot.dinh.website;

/**
 * Created by: Niklas
 * Date: 14.06.2017
 * Time: 14:56
 */

public class Payload {

    public static final String ENDPOINT = "https://dreambot.org/forums/index.php?app=members&module=messaging&section=send&do=send";
    public static final String REFER = "https://dreambot.org/forums/index.php?app=members&module=messaging&section=send&do=form";

    public static String getPayload(String receiver, String title, String message, String token, String ckEditor, String key) {
        return "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"entered_name\"" + "\n" +
                "\n" +
                receiver + "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"inviteUsers\"" + "\n" +
                "\n\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"sendType\"" + "\n" +
                "\n" +
                "invite" + "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"msg_title\"" + "\n" +
                "\n" +
                title + "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"isRte\"" + "\n" +
                "\n" +
                "1" + "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"noSmilies\"" + "\n" +
                "\n" +
                "0" + "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"" + ckEditor + "\"" + "\n" +
                "\n" +
                "0" + "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"Post\"" + "\n" +
                "\n" +
                "<p>" + message + "</p>" + "\n" +
                "\n" +
                "--" + key + "\n" +
                "Content-Disposition: form-data; name=\"topicID\"" + "\n" +
                "\n" +
                "0" + "\n" +
                "--" + key + "\n" +
                "Content-Disposition:form - data; " + "name=\"postKey\"" + "\n" +
                "\n" +
                token + "\n" +
                "--" + key + "\n" +
                "Content-Disposition:form - data; " + "name=\"auth_key\"" + "\n" +
                "\n" +
                "7308e446c8f2762f44c4664b487fb34b" + "\n" +
                "--" + key + "\n" +
                "Content-Disposition:form - data; " + "name=\"dosubmit\"" + "\n" +
                "\n" +
                "Send Message" + "\n" +
                "--" + key + "--";
    }
}
