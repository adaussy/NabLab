/**
 * generated by Xtext 2.12.0
 */
package fr.cea.nabla.nabla.provider;


import fr.cea.nabla.nabla.FunctionDeclarationBlock;
import fr.cea.nabla.nabla.NablaFactory;
import fr.cea.nabla.nabla.NablaPackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link fr.cea.nabla.nabla.FunctionDeclarationBlock} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class FunctionDeclarationBlockItemProvider extends DeclarationBlockItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionDeclarationBlockItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns FunctionDeclarationBlock.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/FunctionDeclarationBlock"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_FunctionDeclarationBlock_type");
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(FunctionDeclarationBlock.class)) {
			case NablaPackage.FUNCTION_DECLARATION_BLOCK__FUNCTIONS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createNablaModule()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createImport()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createDeclarationBlock()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createJob()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createInstruction()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createTimeIterator()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createSpaceIterator()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createIteratorRangeOrRef()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createSpaceIteratorRange()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createSpaceIteratorRef()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createScalarVarDefinition()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createVarGroupDeclaration()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createVar()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createScalarVar()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createArrayVar()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createConnectivity()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createItemArgType()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createFunction()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createFunctionArg()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createReduction()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createReductionArg()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createExpression()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createVarRef()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createTimeIteratorRef()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createConnectivityDeclarationBlock()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createFunctionDeclarationBlock()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createInstructionJob()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createTimeLoopJob()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createInstructionBlock()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createAffectation()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createLoop()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createIf()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createOr()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createAnd()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createEquality()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createComparison()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createPlus()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createMinus()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createMulOrDiv()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createParenthesis()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createUnaryMinus()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createNot()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createIntConstant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createRealConstant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createReal2Constant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createReal3Constant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createBoolConstant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createRealXCompactConstant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createMinConstant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createMaxConstant()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createFunctionCall()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.FUNCTION_DECLARATION_BLOCK__FUNCTIONS,
				 NablaFactory.eINSTANCE.createReductionCall()));
	}

}
