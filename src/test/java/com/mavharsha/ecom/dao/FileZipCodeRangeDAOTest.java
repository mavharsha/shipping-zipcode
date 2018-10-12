package com.mavharsha.ecom.dao;

import com.mavharsha.ecom.util.FileUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtil.class})
public class FileZipCodeRangeDAOTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /*
     * Exceptions
     * */
    @Test
    public void testShouldThrowExceptionWhenFileIsNotFound() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("resource anotherFile.json not found.");
        IZipCodeRangeDAO zipCodeRangeDAO = new FileZipCodeRangeDAO("anotherFile.json");
        zipCodeRangeDAO.getListOfZipCodeRanges();
    }


    @Test
    public void testShouldReturnZipCodeRanges() throws IOException {
        String expected = "[ { \"lowerRange\": {\"code\": 94133}, \"upperRange\": {\"code\": 94133} }," +
                " { \"lowerRange\": {\"code\": 94200}, \"upperRange\": {\"code\": 94299} }, " +
                "{ \"lowerRange\": {\"code\": 94226}, \"upperRange\": {\"code\": 94399} } ]";

        PowerMockito.mockStatic(FileUtil.class);
        when(FileUtil.readFileFromResource(anyString())).thenReturn(expected);

        assertThat(FileUtil.readFileFromResource("test.json")).isNotNull();
        assertThat(FileUtil.readFileFromResource("test.json")).isEqualToIgnoringCase(expected);
        assertThat(FileUtil.readFileFromResource("anotherFile.json"))
                .containsIgnoringCase("{ \"lowerRange\": {\"code\": 94226}, \"upperRange\": {\"code\": 94399}");
    }
}