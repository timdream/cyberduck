package ch.cyberduck.ui.cocoa;

/*
 *  Copyright (c) 2005 David Kocher. All rights reserved.
 *  http://cyberduck.ch/
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Bug fixes, suggestions and comments should be sent to:
 *  dkocher@cyberduck.ch
 */

import ch.cyberduck.core.*;
import ch.cyberduck.ui.cocoa.application.NSDraggingInfo;
import ch.cyberduck.ui.cocoa.application.NSPasteboard;
import ch.cyberduck.ui.cocoa.application.NSTableColumn;
import ch.cyberduck.ui.cocoa.application.NSTableView;
import ch.cyberduck.ui.cocoa.foundation.*;

import org.rococoa.cocoa.foundation.NSUInteger;

import java.util.List;

/**
 * @version $Id$
 */
public class CDBrowserListViewModel extends CDBrowserTableDataSource implements NSTableView.DataSource {

    public CDBrowserListViewModel(CDBrowserController controller) {
        super(controller);
    }

    public int numberOfRowsInTableView(NSTableView view) {
        if(controller.isMounted()) {
            return this.childs(this.controller.workdir()).size();
        }
        return 0;
    }

    public void tableView_setObjectValue_forTableColumn_row(NSTableView view, NSObject value, NSTableColumn tableColumn, int row) {
        if(controller.isMounted()) {
            super.setObjectValueForItem(this.childs(this.controller.workdir()).get(row), value, tableColumn.identifier());
        }
    }

    public NSObject tableView_objectValueForTableColumn_row(NSTableView view, NSTableColumn tableColumn, int row) {
        if(controller.isMounted()) {
            final List<Path> childs = this.childs(this.controller.workdir());
            if(row < childs.size()) {
                return super.objectValueForItem(childs.get(row), tableColumn.identifier());
            }
        }
        return null;
    }

    // ----------------------------------------------------------
    // Drop methods
    // ----------------------------------------------------------

    public int tableView_validateDrop_proposedRow_proposedDropOperation(NSTableView view, NSDraggingInfo draggingInfo, int row, int operation) {
        if(controller.isMounted()) {
            Path destination = controller.workdir();
            final int draggingColumn = view.columnAtPoint(draggingInfo.draggingLocation());
            if(0 == draggingColumn || 1 == draggingColumn) {
                if(row != -1 && row < view.numberOfRows()) {
                    Path p = this.childs(this.controller.workdir()).get(row);
                    if(p.attributes.isDirectory()) {
                        destination = p;
                    }
                }
            }
            return super.validateDrop(view, destination, row, draggingInfo);
        }
        return super.validateDrop(view, null, row, draggingInfo);
    }

    public boolean tableView_acceptDrop_row_dropOperation(NSTableView view, NSDraggingInfo draggingInfo, int row, int operation) {
        if(controller.isMounted()) {
            Path destination = controller.workdir();
            if(row != -1 && row < view.numberOfRows()) {
                destination = this.childs(this.controller.workdir()).get(row);
            }
            return super.acceptDrop(view, destination, draggingInfo);
        }
        return super.acceptDrop(view, null, draggingInfo);
    }

    // ----------------------------------------------------------
    // Drag methods
    // ----------------------------------------------------------

    /**
     * Invoked by view after it has been determined that a drag should begin, but before the drag has been started.
     * The drag image and other drag-related information will be set up and provided by the table view once this call
     * returns with true.
     *
     * @param rows is the list of row numbers that will be participating in the drag.
     * @return To refuse the drag, return false. To start a drag, return true and place the drag data onto pboard (data, owner, and so on).
     */
    public boolean tableView_writeRowsWithIndexes_toPasteboard(NSTableView view, NSIndexSet rowIndexes, NSPasteboard pboard) {
        if(controller.isMounted()) {
            NSMutableArray items = NSMutableArray.arrayWithCapacity(rowIndexes.count().intValue());
            final AttributedList<Path> childs = this.childs(this.controller.workdir());
            for(NSUInteger index = rowIndexes.firstIndex(); index.longValue() != NSIndexSet.NSNotFound; index = rowIndexes.indexGreaterThanIndex(index)) {
                if(index.intValue() == -1) {
                    break;
                }
                items.addObject(NSString.stringWithString(childs.get(index.intValue()).getAbsolute()));
            }
            return super.writeItemsToPasteBoard(view, items, pboard);
        }
        return false;
    }

    public NSArray tableView_namesOfPromisedFilesDroppedAtDestination_forDraggedRowsWithIndexes(NSTableView view, final NSURL dropDestination, NSIndexSet rowIndexes) {
        return this.namesOfPromisedFilesDroppedAtDestination(dropDestination);
    }

//    public NSArray tableView_namesOfPromisedFilesDroppedAtDestination_forDraggedRowsWithIndexes(NSTableView view, final NSURL dropDestination, NSIndexSet rowIndexes) {
//        final NSMutableArray promisedDragNames = NSMutableArray.arrayWithCapacity(rowIndexes.count().intValue());
//        final List<Path> roots = new Collection<Path>();
//        final AttributedList<Path> childs = this.childs(this.controller.workdir());
//        for(NSUInteger index = rowIndexes.firstIndex(); index.intValue() != NSIndexSet.NSNotFound; index = rowIndexes.indexGreaterThanIndex(index)) {
//            Path promisedDragPath = childs.get(index.intValue());
//            promisedDragPath.setLocal(new Local(dropDestination.path(), promisedDragPath.getName()));
//            if(rowIndexes.count().intValue() == 1) {
//                if(promisedDragPath.attributes.isFile()) {
//                    promisedDragPath.getLocal().touch();
//                }
//                if(promisedDragPath.attributes.isDirectory()) {
//                    promisedDragPath.getLocal().mkdir();
//                }
//            }
//            promisedDragNames.addObject(NSString.stringWithString(promisedDragPath.getLocal().getName()));
//            roots.add(promisedDragPath);
//        }
//        final Transfer q = new DownloadTransfer(roots);
//        if(q.numberOfRoots() > 0) {
//            controller.transfer(q);
//        }
//        return promisedDragNames;
//    }
}