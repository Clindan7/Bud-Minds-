package com.innovaturelabs.buddymanagement.util;

import org.springframework.stereotype.Component;

@Component
public class MailTemplate {

    private static final String TR="\"                    <tr>\"";
    private static final String TD="\"                    <td>\"";


    public String emailContent(String name, String email, String password) {
        return "<body marginheight='0' topmargin='0' marginwidth='0' style='margin: 0px; background-color: #f2f3f8; color:#FFFFFF' leftmargin='0'>"
                +
                "    <!-- 100% body table -->" +
                "    <table cellspacing='0' border='0' cellpadding='0' width='100%' bgcolor='#f2f3f8'" +
                "        style='@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;'>"
                +
                "        <tr>" +
                "            <td>" +
                "                <table style='background-color: #f2f3f8; max-width:670px; margin:0 auto;' width='100%' border='0'"
                +
                "                    align='center' cellpadding='0' cellspacing='0'>" +
                TR +
                "                        <td style='height:80px;'>&nbsp;</td>" +
                TR +
                TR +
                "                        <td style='text-align:center;'>" +
                TD +
                TR +
                TR +
                "                        <td style='height:20px;'>&nbsp;</td>" +
                TR +
                TR +
                "                        <td>" +
                "                            <table width='95%' border='0' align='center' cellpadding='0' cellspacing='0'"
                +
                "                                style='max-width:670px; background:#fff; border-radius:5px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);'>"
                +
                TR +
                "                                    <td style='height:40px;'>&nbsp;</td>" +
                TR +
                "                                <tr>" +
                "                                    <td style='padding:0 35px;'>" +
                "<style>" +
                "@import url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;1,200&display=swap');</style>" +
                "<strong  style='font-family: \"Poppins\", sans-serif; display: block; font-size: 13px; margin: 0 0 4px; font-weight: normal; color: #5B5B5B;'>Hi " + name +", welcome to BudMinds"
                +
                "                                        </strong>" +
                "                                        " +
                "                                        <span" +
                "                                            style='display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;'></span>"
                +
                "                                        <p" +
                "                                            style='color:#455056; font-size:18px;line-height:20px; margin:0; font-weight: 500; '>"
                +
                "<strong" +
                "                                                style='display: block; font-size: 13px; margin: 24px 0 4px 0; font-weight:normal; color:rgba(0,0,0,.64); text-align: left;'>Email : " + email + "</strong>" +
                "                                            <strong" +
                "                                                style='display: block; font-size: 13px; margin: 24px 0 4px 0; font-weight:normal; color:rgba(0,0,0,.64); text-align: left;'>Password : " + password + "</strong>" +

                "                                        </p>" +
                "                                    </td>" +
                "                                </tr>" +
                "                                <tr>" +
                "                                    <td style='height:40px;'>&nbsp;</td>" +
                "                                </tr>" +
                "                            </table>" +
                "                        </td>" +
                TR +
                TR +
                "                        <td style='height:20px;'>&nbsp;</td>" +
                TR +
                TR +
                "                        <td style='text-align:center;'>" +
                "                            <p style='font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;'>&copy; <strong>https://buddy-management-prod.innovaturelabs.com</strong> </p>"
                +
                "                        </td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style='height:80px;'>&nbsp;</td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>";
    }
}
