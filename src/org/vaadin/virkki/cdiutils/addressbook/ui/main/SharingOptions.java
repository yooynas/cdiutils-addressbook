package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Localizer;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.CDIEvent;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@VaadinScoped
@SuppressWarnings("serial")
public class SharingOptions extends Window {
    @Inject
    private Lang lang;
    @Inject
    @Preconfigured(labelValueKey = "sharingoptions-content")
    private Label contentLabel;
    @Inject
    @Preconfigured(captionKey = "sharingoptions-gmail", implementation = CheckBox.class)
    private Button gmailCheckBox;
    @Inject
    @Preconfigured(captionKey = "sharingoptions-mac", implementation = CheckBox.class)
    private Button macCheckBox;
    @Inject
    @Preconfigured(captionKey = "ok")
    private Button okButton;

    public final void init() {
        setModal(true);
        setWidth(50.0f, UNITS_PERCENTAGE);
        center();

        addComponent(contentLabel);
        addComponent(gmailCheckBox);
        addComponent(macCheckBox);
        okButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                SharingOptions.this.close();
            }
        });
        addComponent(okButton);
    }

    void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @CDIEvent(Localizer.UPDATE_LOCALIZED_VALUES) final ParameterDTO parameters) {
        setCaption(lang.getText("sharingoptions-caption"));
    }
}
