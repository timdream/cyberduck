package ch.cyberduck.core.threading;

/*
 * Copyright (c) 2002-2013 David Kocher. All rights reserved.
 * http://cyberduck.ch/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Bug fixes, suggestions and comments should be sent to feedback@cyberduck.ch
 */

import ch.cyberduck.core.AbstractTestCase;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.LoginCanceledException;

import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;

import static org.junit.Assert.assertEquals;

/**
 * @version $Id:$
 */
public class AbstractAlertCallbackTest extends AbstractTestCase {

    @Test
    public void testTitle() {
        final AbstractAlertCallback c = new AbstractAlertCallback() {
            @Override
            public void alert(final SessionBackgroundAction<?> action, final BackgroundException failure, final StringBuilder transcript) {
                //
            }
        };
        assertEquals("Error", c.getTitle(new BackgroundException(new LoginCanceledException())));
        assertEquals("I/O Error", c.getTitle(new BackgroundException(new IOException())));
        assertEquals("Network Error", c.getTitle(new BackgroundException(new SocketException("s"))));
    }
}
