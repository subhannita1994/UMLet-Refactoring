/*
 * (c) Copyright 2008 and following years, Julien Tanteri, University of
 * Montreal.
 * 
 * Use and copying of this software and preparation of derivative works based
 * upon this software are permitted. Any copy of this software or of any
 * derivative work must include the above copyright notice of the author, this
 * paragraph and the one after it.
 * 
 * This software is made available AS IS, and THE AUTHOR DISCLAIMS ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND NOT
 * WITHSTANDING ANY OTHER PROVISION CONTAINED HEREIN, ANY LIABILITY FOR DAMAGES
 * RESULTING FROM THE SOFTWARE OR ITS USE IS EXPRESSLY DISCLAIMED, WHETHER
 * ARISING IN CONTRACT, TORT (INCLUDING NEGLIGENCE) OR STRICT LIABILITY, EVEN IF
 * THE AUTHOR IS ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * All Rights Reserved.
 */
package jct.test.rsc.snpsht.parser.revml.sax.fsm.contentloader;

import jct.test.rsc.snpsht.parser.revml.RevMLDocCommonsStrings;
import jct.test.rsc.snpsht.parser.revml.sax.fsm.ErrorStateRevMLState;
import jct.test.rsc.snpsht.parser.sax.AbstractStateSaxFsm;
import jct.test.rsc.snpsht.parser.sax.SimpleSaxFsmParser;
import jct.test.rsc.snpsht.verfilesystem.VerFsFileRev;
import jct.test.rsc.snpsht.verfilesystem.VerFsManager;

public class LoadPrevVersionState extends AbstractStringLoaderState {

	public LoadPrevVersionState(
		SimpleSaxFsmParser fsm,
		AbstractStateSaxFsm previewState,
		VerFsManager manager,
		VerFsFileRev file) {
		super(fsm, previewState, manager, file);
	}

	@Override
	protected void processString(String loadedString) {
		VerFsFileRev prevFile = getManager().getSimpleRevision(loadedString);

		if (prevFile == null) {
			setCallBackSate(new ErrorStateRevMLState(
				getFsm(),
				this,
				getManager(),
				"Can't file previous file '" + loadedString
						+ "' in manager.\nFile ID doesn't exist",
				ErrorStateRevMLState.UNEXPECTED_FILE_ID));
		} else {
			getManager().setPrevRevision(prevFile, getFile());
		}
	}

	@Override
	public String toString() {
		String toRet = "<LoadPrevVersionState>\nPrevious version id: " + getLoadedString();
		return toRet;
	}

	@Override
	protected String getWaitedMarker() {
		return RevMLDocCommonsStrings.FILE_REV_PREV_VERSION_MARKER;
	}

}
