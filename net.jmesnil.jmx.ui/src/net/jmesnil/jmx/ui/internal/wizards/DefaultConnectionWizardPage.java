package net.jmesnil.jmx.ui.internal.wizards;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import net.jmesnil.jmx.resources.ExtensionManager;
import net.jmesnil.jmx.resources.IConnectionProvider;
import net.jmesnil.jmx.resources.IConnectionWrapper;
import net.jmesnil.jmx.resources.internal.providers.DefaultConnectionProvider;
import net.jmesnil.jmx.ui.ConnectionWizardPage;
import net.jmesnil.jmx.ui.JMXMessages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class DefaultConnectionWizardPage extends WizardPage implements
		ConnectionWizardPage {
	private static final String _BLANK_ = ""; //$NON-NLS-1$
	private TabFolder folder;
	private TabItem simpleItem, advancedItem;
	private Text hostText, portText, urlText;
	private Text nameText, userNameText, passwordText;
	private Text advancedNameText, advancedUserNameText, advancedPasswordText;
	private String name, url, userName, password;
	private boolean canComplete = false;

	private void addListeners() {
		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		};
		SelectionListener tabListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				TabItem[] tabs = folder.getSelection();

				if( tabs[0] == simpleItem ) {
					nameText.setText(advancedNameText.getText());
					userNameText.setText(advancedUserNameText.getText());
					passwordText.setText(advancedPasswordText.getText());
				} else {
					advancedNameText.setText(nameText.getText());
					advancedUserNameText.setText(userNameText.getText());
					advancedPasswordText.setText(passwordText.getText());
				}
			}
		};
		nameText.addModifyListener(listener);
		hostText.addModifyListener(listener);
		portText.addModifyListener(listener);
		urlText.addModifyListener(listener);
		userNameText.addModifyListener(listener);
		passwordText.addModifyListener(listener);
		advancedNameText.addModifyListener(listener);
		advancedUserNameText.addModifyListener(listener);
		advancedPasswordText.addModifyListener(listener);
		folder.addSelectionListener(tabListener);
	}
	public DefaultConnectionWizardPage() {
		super(_BLANK_);
		setDescription(JMXMessages.DefaultConnectionWizardPage_Description);
	}

	public void createControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());
		folder = new TabFolder(c, SWT.TOP);
		simpleItem = new TabItem(folder, SWT.NONE);
		simpleItem.setText(JMXMessages.DefaultConnectionWizardPage_Simple);
		simpleItem.setControl(createSimpleConnectionPage(folder));

		advancedItem = new TabItem(folder, SWT.NONE);
		advancedItem.setText(JMXMessages.DefaultConnectionWizardPage_Advanced);
		advancedItem.setControl(createAdvancedConnectionPage(folder));
		setControl(c);

		addListeners();
		validate();
	}

	private Control createSimpleConnectionPage(Composite parent) {
		Composite fieldComposite = new Composite(parent, SWT.BORDER);
		fieldComposite.setLayout(new GridLayout(2, false));

		GridData data = new GridData(GridData.FILL_BOTH);

		// 0 - name
		Label nameLabel = new Label(fieldComposite, SWT.CENTER);
		nameLabel.setText(JMXMessages.DefaultConnectionWizardPage_Name);

		nameText = new Text(fieldComposite, SWT.BORDER);
		nameText.setText(getNextName());
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(data);

		// 1 host label
		Label label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_Host);

		// 2 host text entry
		hostText = new Text(fieldComposite, SWT.BORDER);
		hostText.setText("localhost"); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		hostText.setLayoutData(data);

		// 3 port label
		label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_Port);

		// 4 port text entry
		portText = new Text(fieldComposite, SWT.BORDER);
		portText.setTextLimit(5);
		portText.setText("3000"); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		portText.setLayoutData(data);

		// 5 user name label
		label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_Username);

		// 6 user name text entry
		userNameText = new Text(fieldComposite, SWT.BORDER);
		userNameText.setText(_BLANK_);

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		userNameText.setLayoutData(data);

		// 7 password label
		label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_Password);

		// 8 user name text entry
		passwordText = new Text(fieldComposite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setText(_BLANK_);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		passwordText.setLayoutData(data);

		return fieldComposite;

	}

	private Control createAdvancedConnectionPage(Composite parent) {
		Composite fieldComposite = new Composite(parent, SWT.BORDER);
		fieldComposite.setLayout(new GridLayout(2, false));

		GridData data = new GridData(GridData.FILL_BOTH);

		// 0 - name
		Label nameLabel = new Label(fieldComposite, SWT.CENTER);
		nameLabel.setText(JMXMessages.DefaultConnectionWizardPage_Name);

		advancedNameText = new Text(fieldComposite, SWT.BORDER);
		advancedNameText.setText(getNextName());
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		advancedNameText.setLayoutData(data);

		// 1 host label
		Label label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_JMX_URL);

		// 2 URL text entry
		urlText = new Text(fieldComposite, SWT.BORDER);
		urlText.setText("service:jmx:rmi:"); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		urlText.setLayoutData(data);

		// 3 user name label
		label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_Username);

		// 4 user name text entry
		advancedUserNameText = new Text(fieldComposite, SWT.BORDER);
		advancedUserNameText.setText(_BLANK_);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		advancedUserNameText.setLayoutData(data);

		// 5 password label
		label = new Label(fieldComposite, SWT.CENTER);
		label.setText(JMXMessages.DefaultConnectionWizardPage_Password);

		// 6 user name text entry
		advancedPasswordText = new Text(fieldComposite, SWT.BORDER
				| SWT.PASSWORD);
		advancedPasswordText.setText(_BLANK_);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		advancedPasswordText.setLayoutData(data);

		return fieldComposite;
	}

	protected String getNextName() {
		String name;
		int count = 1;
		if( nameTaken( JMXMessages.DefaultConnectionWizardPage_Default_Name )) {
			do {
				name = JMXMessages.DefaultConnectionWizardPage_Default_Name + " " + count++; //$NON-NLS-1$
			} while( nameTaken(name));
			return name;
		} else
			return JMXMessages.DefaultConnectionWizardPage_Default_Name;
	}

	protected void validate() {
		// TODO Validation
		if (folder.getSelectionIndex() == 0) {
			name = nameText.getText();
			userName = userNameText.getText();
			password = passwordText.getText();
			if (hostText.getText().equals(_BLANK_)) {
				showError("",
						"");
				return;
			}
			try {
				InetAddress.getByName(hostText.getText());
			} catch (UnknownHostException e) {
				showError("",
						"");
				return;
			}
			String host = hostText.getText();
			if (portText.getText().equals(_BLANK_)) {
				showError("",
						"");
				return;
			}
			int port;
			try {
				port = Integer.parseInt(portText.getText());
				if (port < 1 || port > 0xffff) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				showError("",
						"");
				return;
			}
			url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else if (folder.getSelectionIndex() == 1) {
			name = advancedNameText.getText();
			userName = advancedUserNameText.getText();
			password = advancedPasswordText.getText();

			if (urlText.getText().equals(_BLANK_)) {
				showError("",
						"");
				return;
			}
			url = urlText.getText();
		}

		// now validate name
		if( name == null || nameTaken(name)) {
			showError("", "");
			return;
		}
		clearMessage();
	}

	protected void clearMessage() {
		setErrorMessage(null);
		getContainer().updateMessage();
	}

	protected void showError(String one, String two) {
		setErrorMessage("There's an error somewhere");
		getContainer().updateMessage();
	}

	protected boolean nameTaken(String s) {
		IConnectionProvider provider = ExtensionManager.getProvider(DefaultConnectionProvider.PROVIDER_ID);
		IConnectionWrapper[] connections = provider.getConnections();
		for( int i = 0; i < connections.length; i++ ) {
			if( provider.getName(connections[i]).equals(s)) {
				return true;
			}
		}
		return false;
	}

	String getURL() {
		return url;
	}

	String getUserName() {
		return userName;
	}

	String getPassword() {
		return password;
	}

	public IConnectionWrapper getConnection() throws CoreException {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put(DefaultConnectionProvider.ID, name);
		map.put(DefaultConnectionProvider.URL, url);
		map.put(DefaultConnectionProvider.USERNAME, userName);
		map.put(DefaultConnectionProvider.PASSWORD, password);
		IConnectionProvider provider = ExtensionManager.getProvider(DefaultConnectionProvider.PROVIDER_ID);

		return provider.createConnection(map);
	}
}