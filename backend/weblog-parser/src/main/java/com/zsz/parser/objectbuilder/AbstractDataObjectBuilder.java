package com.zsz.parser.objectbuilder;

import com.zsz.iplocation.IpLocationParser;
import com.zsz.parser.dataobject.BaseDataObject;
import com.zsz.parser.utils.ColumnReader;
import com.zsz.prepaser.PreParsedLog;
import eu.bitwalker.useragentutils.UserAgent;

import java.util.List;

public abstract class AbstractDataObjectBuilder {

    public abstract String getCommand();

    public abstract List<BaseDataObject> doBuildDataObjects(PreParsedLog preParsedLog);

    public void fillCommonBaseDataObjectValue(BaseDataObject baseDataObject,
                                              PreParsedLog preParsedLog, ColumnReader columnReader) {
        baseDataObject.setProfileId(preParsedLog.getProfileId());
        baseDataObject.setServerTimeString(preParsedLog.getServerTime().toString());

        baseDataObject.setUserId(columnReader.getStringValue("gsuid"));
        baseDataObject.setTrackerVersion(columnReader.getStringValue("gsver"));
        baseDataObject.setPvId(columnReader.getStringValue("pvid"));
        baseDataObject.setCommand(columnReader.getStringValue("gscmd"));

        //结合ip位置信息
        baseDataObject.setClientIp(preParsedLog.getClientIp().toString());
        baseDataObject.setIpLocation(IpLocationParser.parse(preParsedLog.getClientIp().toString()));
        //解析UserAgent信息
        baseDataObject.setUserAgent(preParsedLog.getUserAgent().toString());
        baseDataObject.setUserAgentInfo(UserAgent.parseUserAgentString(preParsedLog.getUserAgent().toString()));

    }

}
