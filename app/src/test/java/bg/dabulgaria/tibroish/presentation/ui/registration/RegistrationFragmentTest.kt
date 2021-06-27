package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Build.VERSION_CODES.LOLLIPOP
import bg.dabulgaria.tibroish.TestApplication
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = LOLLIPOP, application = TestApplication::class)
public class RegistrationFragmentTest {

    @Test
    public fun testTest() {
        assertThat(true).isTrue()
    }
}