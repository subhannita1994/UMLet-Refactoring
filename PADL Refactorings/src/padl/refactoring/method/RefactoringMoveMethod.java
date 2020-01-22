/*******************************************************************************
 * Copyright (c) 2001-2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/
package padl.refactoring.method;

import java.util.Iterator;
import java.util.List;
import padl.kernel.IAbstractLevelModel;
import padl.kernel.IClass;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IMethod;
import padl.refactoring.exception.ModelDeclarationException;

/**
 * @author Saliha Bouden
 * @since 2006/03/31
 * 
 */
public class RefactoringMoveMethod extends RefactoringMethod {

	public RefactoringMoveMethod(final IAbstractLevelModel anAbstractLevelModel) {
		super(anAbstractLevelModel);
	}

	/**
	 * Cette m�thode permet de v�rifier les pres conditions de Move Method sans
	 * la surcharge de m�thodes
	 * @throws ModelDeclarationException 
	 * @throws ModelDeclarationException 
	 */
	private IMethod checkPreConditionsOfMoveMethod(
		String methodName,
		IClass sourceClass,
		IClass targetClass) throws ModelDeclarationException {

		final IMethod theMethod =
			this.getMethodToRefactor(sourceClass.getDisplayName(), methodName);
		if (theMethod != null) {
			//			final String aNewMethodID = this.createNewMethodSignature(
			//				theMethod,
			//				theMethod.getName());

			if (!doesMethodInvoquedInClass(
				theMethod.getDisplayID(),
				sourceClass)) {

				// si la m�thode est final il faut tester que le nom de la m�thode ne doit pas exister dans les
				// sous classes de la classe de destination.
				if (theMethod.isFinal()) {
					final List listOfSubClasses =
						this.doesNewMethodExist(
							targetClass.getIteratorOnInheritingEntities(),
							theMethod.getDisplayName());
					if (!listOfSubClasses.isEmpty()) {
						throw new ModelDeclarationException(
							"Refactoring Move Method  is impossible to apply because "
									+ theMethod.getDisplayName()
									+ "  declared Final exist in subclasses of target class  "
									+ sourceClass.getDisplayName());
					}

				}
				else // !(theMethod.isFinal())
				// si la m�thode n'est pas final il faut s'assurer qu'aucune m�thode dans les super classes de la classe
				//  de destination ne doit avoir le meme non avec une visibilit� final.
				{
					List listOfSuperClasses =
						this.doesNewMethodExist(
							targetClass.getIteratorOnInheritedEntities(),
							methodName);
					if (!listOfSuperClasses.isEmpty()) {

						final Iterator iterator =
							targetClass.getIteratorOnInheritedEntities();
						while (iterator.hasNext()) {
							final IFirstClassEntity theEntity =
								(IFirstClassEntity) iterator.next();
							final IMethod aMethod =
								this.getMethodToRefactor(
									theEntity.getDisplayName(),
									theMethod.getDisplayName());
							if (aMethod != null) {
								if (aMethod.isFinal()) {
									throw new ModelDeclarationException(
										"Refactoring Move Method  is impossible to apply because "
												+ theMethod.getDisplayName()
												+ "  exist in the superclasse of target class "
												+ sourceClass.getDisplayName()
												+ " and declared final");
								}
							}
						}

					}
					return theMethod;
				}

			}
			else
				// TODO: Add "{" and "}" even if there is only one statement.
				// TODO: You could use you own exception here?
				throw new ModelDeclarationException(
					"Refactoring Move Method  is impossible to apply because "
							+ theMethod.getDisplayName()
							+ "  is invoqued in the definition classe "
							+ sourceClass.getDisplayName());
		}

		return null;
	}
	public void moveMethod(
		final String methodName,
		final String sourceClassName,
		final String targetClassName) {

		final IClass sourceClass =
			(IClass) this.abstractLevelModel
				.getTopLevelEntityFromID(sourceClassName);
		final IClass targetClass =
			(IClass) this.abstractLevelModel
				.getTopLevelEntityFromID(targetClassName);
		try {
			IMethod aMethod =
				this.checkPreConditionsOfMoveMethod(
					methodName,
					sourceClass,
					targetClass);
			if (aMethod != null) {
				targetClass.addConstituent(aMethod);
				sourceClass.removeConstituentFromID(aMethod.getID());
			}
		}
		catch (ModelDeclarationException e) {

			e.printStackTrace();
		}
	}
	/**
	 * Cette m�thode permet de v�rifier les pres conditions de Move Method avec
	 * la surcharge de m�thodes
	 * @throws ModelDeclarationException 
	 * @throws ModelDeclarationException 
	 */
	private IMethod checkPreConditionsOfMoveMethodAcceptOverloading(
		String methodName,
		IClass sourceClass,
		IClass targetClass) throws ModelDeclarationException {

		final IMethod theMethod =
			this.getMethodToRefactor(sourceClass.getDisplayName(), methodName);
		if (theMethod != null) {
			final String aNewMethodID =
				this.createNewMethodSignature(
					theMethod,
					theMethod.getDisplayName());

			// TODO: "Invoqued" is "Invoked" :-)
			if (!doesMethodInvoquedInClass(
				theMethod.getDisplayID(),
				sourceClass)) {

				// si la m�thode est final il faut tester que le nom de la m�thode ne doit pas exister dans les
				// sous classes de la classe de destination.
				if (theMethod.isFinal()) {
					final List listOfSubClasses =
						this.doesNewMethodExist(
							targetClass.getIteratorOnInheritingEntities(),
							theMethod.getDisplayName());
					if (!listOfSubClasses.isEmpty()) {
						throw new ModelDeclarationException(
							"Refactoring Move Method  is impossible to apply because "
									+ theMethod.getDisplayName()
									+ "  declared Final exist in subclasses of target class  "
									+ sourceClass.getDisplayName());
					}

				}
				else // !(theMethod.isFinal())
				// si la m�thode n'est pas final il faut s'assurer qu'aucune m�thode dans les super classes de la classe
				//  de destination ne doit avoir le meme non avec une visibilit� final.
				{
					List listOfSuperClasses =
						this.doesNewMethodExistWithOverloading(
							targetClass.getIteratorOnInheritedEntities(),
							aNewMethodID);
					if (!listOfSuperClasses.isEmpty()) {

						final Iterator iterator =
							targetClass.getIteratorOnInheritedEntities();
						while (iterator.hasNext()) {
							final IFirstClassEntity theEntity =
								(IFirstClassEntity) iterator.next();
							final IMethod aMethod =
								this.getMethodToRefactor(
									theEntity.getDisplayName(),
									theMethod.getDisplayName());
							if (aMethod != null) {
								if (aMethod.isFinal()) {
									throw new ModelDeclarationException(
										"Refactoring Move Method  is impossible to apply because "
												+ theMethod.getDisplayName()
												+ "  exist in the superclasse of target class "
												+ sourceClass.getDisplayName()
												+ " and declared final");
								}
							}
						}

					}
					return theMethod;
				}

			}
			else
				throw new ModelDeclarationException(
					"Refactoring Move Method  is impossible to apply because "
							+ theMethod.getDisplayName()
							+ "  is invoqued in the definition classe "
							+ sourceClass.getDisplayName());
		}

		return null;
	}

	public void moveMethodAcceptOverloading(
		final String methodName,
		final String sourceClassName,
		final String targetClassName) {

		final IClass sourceClass =
			(IClass) this.abstractLevelModel
				.getTopLevelEntityFromID(sourceClassName);
		final IClass targetClass =
			(IClass) this.abstractLevelModel
				.getTopLevelEntityFromID(targetClassName);
		try {
			IMethod aMethod =
				this.checkPreConditionsOfMoveMethodAcceptOverloading(
					methodName,
					sourceClass,
					targetClass);
			if (aMethod != null) {
				targetClass.addConstituent(aMethod);
				sourceClass.removeConstituentFromID(aMethod.getID());
			}
		}
		catch (ModelDeclarationException e) {

			e.printStackTrace();
		}
	}
}
