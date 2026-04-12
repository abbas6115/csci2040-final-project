package csci2040u.bytecouncil;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.shared.communication.PushMode;

//allows the server to fetch for videos and image from tmdb
@Push(PushMode.AUTOMATIC)
public class AppShell implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        // Correct method is addFavicon (lowercase 'i')
//        settings.addFavicon("icon", "icons/icon.png");
    }
}
