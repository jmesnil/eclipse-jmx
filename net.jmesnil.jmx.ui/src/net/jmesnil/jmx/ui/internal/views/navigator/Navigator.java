package net.jmesnil.jmx.ui.internal.views.navigator;

import net.jmesnil.jmx.ui.internal.actions.NewConnectionAction;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;

public class Navigator extends CommonNavigator {
	public static final String VIEW_ID = "net.jmesnil.jmx.ui.internal.views.navigator.MBeanExplorer"; //$NON-NLS-1$
	public Navigator() {
		super();
	}
	protected IAdaptable getInitialInput() {
		return this;
	}
	public void createPartControl(Composite aParent) {
		fillActionBars();
		super.createPartControl(aParent);
	}

	public void fillActionBars() {
	    getViewSite().getActionBars().getToolBarManager().add(new NewConnectionAction());
	    getViewSite().getActionBars().updateActionBars();
	}

}
