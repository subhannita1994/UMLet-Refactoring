/* (c) Copyright 2001 and following years, Yann-Ga�l Gu�h�neuc,
 * University of Montreal.
 * 
 * Use and copying of this software and preparation of derivative works
 * based upon this software are permitted. Any copy of this software or
 * of any derivative work must include the above copyright notice of
 * the author, this paragraph and the one after it.
 * 
 * This software is made available AS IS, and THE AUTHOR DISCLAIMS
 * ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, AND NOT WITHSTANDING ANY OTHER PROVISION CONTAINED HEREIN,
 * ANY LIABILITY FOR DAMAGES RESULTING FROM THE SOFTWARE OR ITS USE IS
 * EXPRESSLY DISCLAIMED, WHETHER ARISING IN CONTRACT, TORT (INCLUDING
 * NEGLIGENCE) OR STRICT LIABILITY, EVEN IF THE AUTHOR IS ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * All Rights Reserved.
 */
package ptidej.viewer.helper;

import padl.kernel.impl.ConstituentsRepository;
import sad.codesmell.detection.CodeSmellDetectionsRepository;
import sad.codesmell.detection.ICodeSmellDetection;
import sad.designsmell.detection.DesignSmellDetectionsRepository;
import sad.designsmell.detection.IDesignSmellDetection;

public class RepositoriesAccessor {
	private static RepositoriesAccessor UniqueInstance;
	public static RepositoriesAccessor getInstance() {
		if (RepositoriesAccessor.UniqueInstance == null) {
			RepositoriesAccessor.UniqueInstance = new RepositoriesAccessor();
		}
		return RepositoriesAccessor.UniqueInstance;
	}

	private ICodeSmellDetection[] codeSmellDetections;
	private ConstituentsRepository constituentsRepository;
	private IDesignSmellDetection[] designSmellDetections;
	private RepositoriesAccessor() {
		final CodeSmellDetectionsRepository codeSmellDetectionsRepository =
			CodeSmellDetectionsRepository.getInstance();
		this.codeSmellDetections = codeSmellDetectionsRepository.getCodeSmellDetections();

		final DesignSmellDetectionsRepository designSmellDetectionsRepository =
			DesignSmellDetectionsRepository.getInstance();
		this.designSmellDetections = designSmellDetectionsRepository.getDesignSmellDetections();

		this.constituentsRepository = ConstituentsRepository.getInstance();
	}
	public ICodeSmellDetection[] getCodeSmellDetections() {
		return this.codeSmellDetections;
	}
	public ConstituentsRepository getConstituentRepository() {
		return this.constituentsRepository;
	}
	public IDesignSmellDetection[] getDesignSmellDetections() {
		return this.designSmellDetections;
	}
}
