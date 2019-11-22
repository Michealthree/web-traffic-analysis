package com.zsz.parser.objectbuilder.helper;

import com.zsz.metadata.api.ProfileConfigManager;
import com.zsz.metadata.api.impl.MongoProfileConfigManager;
import com.zsz.parser.configuration.TargetConfigMatcher;
import com.zsz.parser.configuration.loader.ProfileConfigLoader;
import com.zsz.parser.configuration.loader.impl.DefaultProfileConfigLoader;
import com.zsz.parser.objectbuilder.MockProfileConfigManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TargetPageAnalyzerTest {
    private TargetPageAnalyzer targetPageAnalyzer;

    @Before
    public void setUp() throws Exception {
        ProfileConfigManager profileConfigManager = new MockProfileConfigManager();
        ProfileConfigLoader profileConfigLoader = new DefaultProfileConfigLoader(profileConfigManager);
        targetPageAnalyzer = new TargetPageAnalyzer(profileConfigLoader);
    }

    @Test
    public void getTargetHits() {
        List<TargetConfigMatcher> targetConfigMatchers =
                targetPageAnalyzer.getTargetHits(702, "https://www.underarmour.cn/shopping/checkoutLogin");
        Assert.assertEquals(1, targetConfigMatchers.size());
        TargetConfigMatcher matcher = targetConfigMatchers.get(0);
        Assert.assertEquals("1", matcher.getKey());
        Assert.assertEquals("test target", matcher.getTargetName());
        Assert.assertEquals(true, matcher.isActive());
    }
}