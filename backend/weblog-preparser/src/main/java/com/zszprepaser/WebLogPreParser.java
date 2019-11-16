package com.zszprepaser;

/**
 * 2.
 * 用于解析原始日志
 *
 * #Fields: date time s-ip cs-method cs-uri-stem cs-uri-query s-port cs-username c-ip cs(User-Agent)
 * 2018-06-16 13:41:39 10.200.200.98 GET /gs.gif gsver=3.8.0.9&gscmd=pv&gssrvid=GWD-000702&gsuid=28872593x9769t21&gssid=t291319151wwp6d11&pvid=291355119hvjhz11&gsltime=1529164311206&gstmzone=8&rd=i3u8k&gstl=Under%20Armour%7C%E5%AE%89%E5%BE%B7%E7%8E%9B%E4%B8%AD%E5%9B%BD%E5%AE%98%E7%BD%91%20-%20UA%E8%BF%90%E5%8A%A8%E5%93%81%E7%89%8C%E4%B8%93%E5%8D%96%EF%BC%8C%E7%BE%8E%E5%9B%BD%E9%AB%98%E7%AB%AF%E8%BF%90%E5%8A%A8%E7%A7%91%E6%8A%80%E5%93%81%E7%89%8C&gscp=2%3A%3Acookie%2520not%2520exist.%7C%7C3%3A%3Acookie%2520not%2520exist.%7C%7C4%3A%3Acookie%2520not%2520exist.%7C%7C5%3A%3Acookie%2520not%2520exist.%7C%7C6%3A%3Acookie%2520not%2520exist.&gsce=1&gsclr=24&gsje=0&gsst=0&gswh=759&gsph=5461&gspw=1519&gssce=1&gsscr=1536*864&dedupid=29135511vx5ccp11&gsurl=https%3A%2F%2Fwww.underarmour.cn%2F&gsorurl=https%3A%2F%2Fwww.underarmour.cn 80 - 58.210.35.226 Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/67.0.3396.87+Safari/537.36
 * 2018-06-16 13:41:40 10.200.200.98 GET /gs.gif gsver=3.8.0.9&gscmd=mc&gssrvid=GWD-000702&gsuid=28872593x9769t21&gssid=t291319151wwp6d11&pvid=291355119hvjhz11&gsltime=1529164325245&gstmzone=8&rd=ioz8v&btn=0&ubtype=click&tgcg=selector&tgpth=div%23wrapper%7B26%7D%3Eheader.header.events-header%7B2%7D%3Ediv&tgtag=div&tgcls=header-container%20container-width%20float-clearfix&tgidx=1&ubid=29135525st712511&gspver=ver20171221&gsmcoffsetx=856&gsmcoffsety=58&gselmw=1519&gselmh=60&gsmcelmx=856&gsmcelmy=24&gstl=Under%20Armour%7C%E5%AE%89%E5%BE%B7%E7%8E%9B%E4%B8%AD%E5%9B%BD%E5%AE%98%E7%BD%91%20-%20UA%E8%BF%90%E5%8A%A8%E5%93%81%E7%89%8C%E4%B8%93%E5%8D%96%EF%BC%8C%E7%BE%8E%E5%9B%BD%E9%AB%98%E7%AB%AF%E8%BF%90%E5%8A%A8%E7%A7%91%E6%8A%80%E5%93%81%E7%89%8C&gssn=0&gsscr=1536*864&gsorurl=https%3A%2F%2Fwww.underarmour.cn&gsmcurl=https%3A%2F%2Fwww.underarmour.cn%2F 80 - 58.210.35.226 Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/67.0.3396.87+Safari/537.36
 */
public class WebLogPreParser {
    public static PreParsedLog parse(String line) {
        if (line.startsWith("#")) {
            return null;
        } else {
            PreParsedLog preParsedLog = new PreParsedLog();
            String[] temps = line.split(" ");
            preParsedLog.setServerTime(temps[0] + " " + temps[1]);
            preParsedLog.setServerIp(temps[2]);
            preParsedLog.setMethod(temps[3]);
            preParsedLog.setUriStem(temps[4]);
            String queryString = temps[5];
            preParsedLog.setQueryString(queryString);
            String[] queryStrTemps = queryString.split("&");
            String command = queryStrTemps[1].split("=")[1];
            preParsedLog.setCommand(command);
            String profileIdStr = queryStrTemps[2].split("=")[1];
            preParsedLog.setProfileId(getProfileId(profileIdStr));
            preParsedLog.setServerPort(Integer.parseInt(temps[6]));
            preParsedLog.setClientIp(temps[8]);
            preParsedLog.setUserAgent(temps[9].replace("+", " "));
            String tempTime = preParsedLog.getServerTime().replace("-", "");
            preParsedLog.setDay(Integer.parseInt(tempTime.substring(0, 8)));
            preParsedLog.setMonth(Integer.parseInt(tempTime.substring(0, 6)));
            preParsedLog.setYear(Integer.parseInt(tempTime.substring(0, 4)));
            return preParsedLog;
        }
    }

    private static int getProfileId(String profileIdStr) {
        return Integer.valueOf(profileIdStr.substring(profileIdStr.indexOf("-") + 1));
    }
}
