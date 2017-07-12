package uk.gov.hmcts.reform.cmc.pdf.service.client.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PreconditionsTest {

    @Test(expected = NullPointerException.class)
    public void notEmptyShouldThrowNullPointerExceptionWhenGivenEmptyArray() {
        Preconditions.requireNonEmpty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notEmptyShouldThrowIllegalArgumentExceptionWhenGivenEmptyArray() {
        Preconditions.requireNonEmpty(new byte[] {});
    }

    @Test
    public void notEmptyShouldNotThrowWhenGivenNonEmptyArray() {
        Throwable throwable = catchThrowable(() -> Preconditions.requireNonEmpty(new byte[] { 12, 34, 56, 78 }));
        assertThat(throwable).isNull();
    }
}
