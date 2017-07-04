package uk.gov.hmcts.reform.cmc.pdf.service.client.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PreconditionsTest {

    @Test(expected = NullPointerException.class)
    public void notNullShouldThrowNPEWhenGivenNullInput() {
        Preconditions.checkNotNull(null);
    }

    @Test
    public void notNullShouldNotThrowNPEWhenGivenNonNullInput() {
        Throwable throwable = catchThrowable(() -> Preconditions.checkNotNull(new Object()));
        assertThat(throwable).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void notEmptyShouldThrowIllegalArgumentExceptionWhenGivenEmptyArray() {
        Preconditions.checkNotEmpty(new byte[] {});
    }

    @Test
    public void notEmptyShouldNotThrowWhenGivenNonEmptyArray() {
        Throwable throwable = catchThrowable(() -> Preconditions.checkNotEmpty(new byte[] { 12, 34, 56, 78 }));
        assertThat(throwable).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validURLShouldThrowIllegalArgumentExceptionWhenGivenGarbageString() {
        Preconditions.checkValidURL("this is not a URL");
    }

    @Test
    public void validURLShouldNotThrowWhenValidURLString() {
        Throwable throwable = catchThrowable(() -> Preconditions.checkValidURL("http://some-host:1234/api"));
        assertThat(throwable).isNull();
    }

}
