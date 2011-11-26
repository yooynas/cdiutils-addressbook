package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.AddressBookApplication;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchViewImpl;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;

import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SessionScoped
public class MainViewImpl extends AbstractView implements MainViev {
	@Inject
	private Instance<NavigationTree> tree;
	@Inject
	private Instance<Toolbar> toolbar;
	@Inject
	@Preconfigured
	private HorizontalSplitPanel horizontalSplit;
	@Inject
	private Instance<AddressBookApplication> application;

	// Views
	@Inject
	private Instance<ListViewImpl> listView;
	@Inject
	private Instance<SearchViewImpl> searchView;
	@Inject
	private HelpWindow helpWindow;
	@Inject
	private SharingOptions sharingOptions;
	@Inject
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;

	@Override
	protected void initView() {
		setSizeFull();

		VerticalLayout mainLayout = new VerticalLayout();
		setCompositionRoot(mainLayout);
		mainLayout.setSizeFull();

		toolbar.get().init();
		mainLayout.addComponent(toolbar.get());

		mainLayout.addComponent(horizontalSplit);
		mainLayout.setExpandRatio(horizontalSplit, 1);

		horizontalSplit.setSplitPosition(200, UNITS_PIXELS);
		horizontalSplit.setFirstComponent(tree.get());

		helpWindow.init();
		sharingOptions.init();

		tree.get().setValue(NavigationTree.SHOW_ALL);
	}

	@Override
	public void setView(Class<? extends View> viewClass,
			boolean selectInNavigationTree) {
		AbstractView view = null;
		if (SearchView.class.isAssignableFrom(viewClass)) {
			view = searchView.get();
		} else if (ListView.class.isAssignableFrom(viewClass)) {
			view = listView.get();
		}
		horizontalSplit.setSecondComponent(view);
		if (selectInNavigationTree) {
			tree.get().setSelectedView(viewClass);
		}
		view.openView();
	}

	@Override
	public void showHelpWindow() {
		if (helpWindow.getParent() == null) {
			application.get().getMainWindow().addWindow(helpWindow);
		}
	}

	@Override
	public void showShareWindow() {
		if (sharingOptions.getParent() == null) {
			application.get().getMainWindow().addWindow(sharingOptions);
		}
	}

	@Override
	public void addSearchToTree(SearchFilter searchFilter) {
		tree.get().addSearchToTree(searchFilter);
	}

}