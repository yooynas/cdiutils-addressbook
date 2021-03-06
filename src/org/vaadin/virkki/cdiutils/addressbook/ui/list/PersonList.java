package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
@VaadinScoped
public class PersonList extends ViewComponent {
    @Inject
    @Preconfigured(nullSelectionAllowed = false, sizeFull = true, immediate = true)
    private Table table;

    public static final Object[] NATURAL_COL_ORDER = new Object[] {
            Person.Fields.firstName.name(), Person.Fields.lastName.name(),
            Person.Fields.email.name(), Person.Fields.phoneNumber.name(),
            Person.Fields.streetAddress.name(),
            Person.Fields.postalCode.name(), Person.Fields.city.name() };

    public final void init() {
        setSizeFull();
        setCompositionRoot(table);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSelectable(true);

        table.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    final com.vaadin.data.Property.ValueChangeEvent event) {
                final Person person = (Person) event.getProperty().getValue();
                if (person != null) {
                    fireViewEvent(ListPresenter.PERSON_SELECTED, person);
                }
            }
        });

        initColumns();
        localize();
    }

    private void initColumns() {
        table.setContainerDataSource(new BeanItemContainer<Person>(Person.class));
        table.setVisibleColumns(NATURAL_COL_ORDER);

        table.addGeneratedColumn(Person.Fields.email.name(),
                new ColumnGenerator() {
                    @Override
                    public Component generateCell(final Table source,
                            final Object itemId, final Object columnId) {
                        final String email = ((Person) itemId).getEmail();
                        return new Link(email, new ExternalResource("mailto:"
                                + email));
                    }
                });

        table.setSortContainerPropertyId(Person.Fields.firstName.name());
    }

    public final void setPersonList(final Collection<Person> people) {
        table.removeAllItems();
        for (final Person person : people) {
            table.addItem(person);
        }
        table.sort();
    }

    public final void addContactToList(final Person person) {
        table.addItem(person);
        table.sort();
    }

    @SuppressWarnings("unchecked")
    public final void applyFilter(final SearchFilter searchFilter) {
        final BeanItemContainer<Person> container = (BeanItemContainer<Person>) table
                .getContainerDataSource();
        // clear previous filters
        container.removeAllContainerFilters();

        if (searchFilter != null) {
            // filter contacts with given filter
            container.addContainerFilter(searchFilter.getPropertyId(),
                    searchFilter.getTerm(), true, false);

            final String propertyName = getText("person-"
                    + String.valueOf(searchFilter.getPropertyId())
                            .toLowerCase());
            /*
             * personlist-searchnotification -text has 3 parameters which are
             * passed in the Lang.getText-method.
             */
            final String notificationText = getText(
                    "personlist-searchnotification", propertyName,
                    searchFilter.getTerm(), container.size());
            getContextWindow().showNotification(notificationText,
                    Notification.TYPE_TRAY_NOTIFICATION);
        }
    }

    public final void setValue(final Person person) {
        table.setValue(person);
    }

    public final Item getSelectedItem() {
        return table.getItem(table.getValue());
    }

    @Override
    protected void localize() {
        for (final Object propertyId : table.getVisibleColumns()) {
            final String header = getText("person-"
                    + String.valueOf(propertyId).toLowerCase());
            table.setColumnHeader(propertyId, header);
        }
    }

}