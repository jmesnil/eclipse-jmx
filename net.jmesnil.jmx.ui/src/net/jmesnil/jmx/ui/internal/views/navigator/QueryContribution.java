package net.jmesnil.jmx.ui.internal.views.navigator;

import java.util.HashMap;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class QueryContribution extends ControlContribution {
	
	private static HashMap<Viewer, QueryContribution> map = 
			new HashMap<Viewer, QueryContribution>();
	
	public static QueryContribution getContributionFor(Viewer v) {
		return map.get(v);
	}

	private String filterText, oldFilterText;
	private HashMap<Object, Boolean> cache = new HashMap<Object, Boolean>();

	public static String getFilterText(Viewer viewer) {
		QueryContribution qc = map.get(viewer);
		if( qc != null ) {
			return qc.filterText;
		}
		return null;
	}

	
	
	private Navigator navigator;
	private Text text;
	private boolean requiresRefine;
	public QueryContribution(final Navigator navigator) {
		super("net.jmesnil.jmx.ui.internal.views.navigator.ControlContribution");
		this.navigator = navigator;
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				map.put(navigator.getCommonViewer(), QueryContribution.this);
			}
		});
	}

	protected Control createControl(Composite parent) {
		text = new Text(parent, SWT.SINGLE | SWT.BORDER );
		text.setToolTipText("Type in a filter"); 
		text.setText("Type in a filter"); 
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				oldFilterText = filterText;
				filterText = text.getText();
				final String old = oldFilterText == null ? "" : oldFilterText;
				final String neww = filterText == null ? "" : filterText;
				
				if( old.equals("") || neww.equals("") || !neww.startsWith(old)) {
					clearCache();
					navigator.getCommonViewer().refresh();
				}  else if(neww.startsWith(old) && !neww.equals(old)) {
					requiresRefine = true;
					navigator.getCommonViewer().refresh();
				}
			} 
		});
		return text;
	}
	
	protected void clearCache() {
		cache = new HashMap<Object,Boolean>();
		requiresRefine = false;
	}
	
	protected boolean cache(Object o, boolean refine, ITreeContentProvider provider) {
		if( !refine ) {
			if( cache.get(o) != null )
				return cache.get(o).booleanValue();
		}
		boolean belongs = false;
		Object tmp;
		Object[] children = provider.getChildren(o);
		for( int i = 0; i < children.length; i++ ) {
			tmp = cache.get(children[i]);
			if( !refine || (tmp != null && ((Boolean)tmp).booleanValue())) {
				belongs |= cache(children[i], refine, provider);
			}
		}
		if( !belongs ) {
			String elementAsString = MBeanExplorerLabelProvider.getText2(o);
			if( elementAsString.contains(filterText))
				belongs = true;
		}
		cache.put(o, new Boolean(belongs));
		return belongs;
	}
		
	public boolean shouldShow(Object element) {
		String filterText = this.filterText;
		if( filterText != null && !("".equals(filterText))) {
			if( navigator.getCommonViewer().getContentProvider() instanceof ITreeContentProvider ) {
				ITreeContentProvider tcp = (ITreeContentProvider)navigator.getCommonViewer().getContentProvider();
				cache(element, requiresRefine, tcp);
			}
			return cache.get(element).booleanValue();
		}
		return true;
	}
	
	protected int computeWidth(Control control) {
		return 100;
	}
	
    public void dispose() {
    	clearCache();
    	oldFilterText = null;
    	filterText = null;
    }
    
}
