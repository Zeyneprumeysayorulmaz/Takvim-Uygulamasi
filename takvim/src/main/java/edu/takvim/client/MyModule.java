package edu.takvim.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.takvim.client.gui.NormalKullaniciSayfasi;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyModule implements EntryPoint {
    VerticalPanel vp = new VerticalPanel();



    private final Messages messages = GWT.create(Messages.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        vp.setSpacing(500);

        Label lblBaslik = new Label("........... " + messages.CALENDAR());
        lblBaslik.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblBaslik.getElement().getStyle().setFontSize(20, Style.Unit.PX);
        vp.add(lblBaslik);

        vp.add(new Label(messages.welcome()));

        vp.add(this::createLogin);
        vp.getElement().getStyle().setLeft(40, Style.Unit.PCT);
        vp.getElement().getStyle().setTop(40, Style.Unit.PCT);
        vp.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);


        FlowPanel mask = new FlowPanel();
        mask.setStylePrimaryName("mask");
        mask.add(vp);
        //  RootLayoutPanel.get().add(mask);
        RootPanel.get("container").add(mask);
    }

    Panel createLogin() {
        FlexTable ft = new FlexTable();
        ft.setCellSpacing(50);

        ft.setWidget(0, 0, new Label(messages.userName()));
        ft.setWidget(0, 1, new TextBox());
        ft.setWidget(1, 0, new Label(messages.password()));
        ft.setWidget(1, 1, new PasswordTextBox());

        Button btnHakkinda = new Button(messages.about());

        ValueListBox<Dil> lbDil = new ValueListBox<>(new AbstractRenderer<Dil>() {
            @Override
            public String render(Dil dil) {
                if (dil != null) {
                    return dil.adi;
                } else {
                    return "";
                }
            }
        });

        Collection<Dil> diller = new ArrayList<>();
        diller.add(new Dil("Türkçe", "tr"));
        diller.add(new Dil("İngilizce", "en"));
        lbDil.setValue(((ArrayList<Dil>) diller).get(0));
        lbDil.setAcceptableValues(diller);

        lbDil.addValueChangeHandler(e -> {
            localizeApp(lbDil.getValue());
        });

        Button btnGiris = new Button(messages.signIn());
        btnGiris.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                vp.clear();
                vp.getElement().getStyle().setLeft(0, Style.Unit.PCT);
                vp.getElement().getStyle().setTop(0, Style.Unit.PCT);
                vp.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

                NormalKullaniciSayfasi normalKullaniciSayfasi = new NormalKullaniciSayfasi();
                normalKullaniciSayfasi.getElement().getStyle().setWidth(100, Style.Unit.PCT);
                normalKullaniciSayfasi.getElement().getStyle().setHeight(100, Style.Unit.PCT);
                vp.add(normalKullaniciSayfasi);
            }
        });

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(20);
        hp.add(btnHakkinda);
        hp.add(lbDil);
        hp.add(btnGiris);
        ft.setWidget(2, 0, hp);

        ft.getFlexCellFormatter().setColSpan(2, 0, 3);

        return ft;
    }

    void localizeApp(Dil dil) {
        if (dil != null) {
            String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
            if (!currentLocale.equals(dil.kodu)) {
                String url = GWT.getModuleBaseURL();
                String newUrl = url.replaceAll("MyModule/", "MyModule.html") + "?locale=" + dil.kodu;
                Window.Location.replace(newUrl);
            }
            GWT.log(currentLocale);
        }
    }


    class Dil {
        String adi;
        String kodu;

        public Dil(String adi, String kodu) {
            this.adi = adi;
            this.kodu = kodu;
        }
    }
}