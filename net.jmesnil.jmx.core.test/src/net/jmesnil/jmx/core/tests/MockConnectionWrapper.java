package net.jmesnil.jmx.core.tests;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

import net.jmesnil.jmx.core.IConnectionProvider;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.core.IJMXRunnable;
import net.jmesnil.jmx.core.tree.Root;

public class MockConnectionWrapper implements IConnectionWrapper {

    public boolean canControl() {
        return false;
    }

    public void connect() throws IOException {
    }

    public void disconnect() throws IOException {
    }

    public IConnectionProvider getProvider() {
        return null;
    }

    public Root getRoot() {
        return null;
    }

    public boolean isConnected() {
        return false;
    }

    public void run(IJMXRunnable runnable) throws CoreException {
    }

}