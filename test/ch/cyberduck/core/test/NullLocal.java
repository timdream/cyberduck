package ch.cyberduck.core.test;

import ch.cyberduck.core.Local;
import ch.cyberduck.core.PathNormalizer;
import ch.cyberduck.core.exception.AccessDeniedException;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @version $Id$
 */
public class NullLocal extends Local {

    public NullLocal(final String parent, final String name) {
        super(parent, name);
    }

    public NullLocal(final Local parent, final String name) {
        super(parent, name);
    }

    public NullLocal(final String name) {
        super(PathNormalizer.normalize(name, false));
    }

    @Override
    public boolean isFile() {
        if(!super.exists()) {
            return true;
        }
        return super.isFile();
    }

    @Override
    public InputStream getInputStream() throws AccessDeniedException {
        return new NullInputStream(0L);
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws AccessDeniedException {
        return new NullOutputStream();
    }
}