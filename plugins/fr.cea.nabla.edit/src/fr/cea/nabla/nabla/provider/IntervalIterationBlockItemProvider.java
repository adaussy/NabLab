/**
 * generated by Xtext 2.19.0
 */
package fr.cea.nabla.nabla.provider;


import fr.cea.nabla.nabla.IntervalIterationBlock;
import fr.cea.nabla.nabla.NablaFactory;
import fr.cea.nabla.nabla.NablaPackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link fr.cea.nabla.nabla.IntervalIterationBlock} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class IntervalIterationBlockItemProvider extends IterationBlockItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IntervalIterationBlockItemProvider(AdapterFactory adapterFactory) {
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

			addToIncludedPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the To Included feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToIncludedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IntervalIterationBlock_toIncluded_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IntervalIterationBlock_toIncluded_feature", "_UI_IntervalIterationBlock_type"),
				 NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO_INCLUDED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
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
			childrenFeatures.add(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__INDEX);
			childrenFeatures.add(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__FROM);
			childrenFeatures.add(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO);
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
	 * This returns IntervalIterationBlock.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/IntervalIterationBlock"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		IntervalIterationBlock intervalIterationBlock = (IntervalIterationBlock)object;
		return getString("_UI_IntervalIterationBlock_type") + " " + intervalIterationBlock.isToIncluded();
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

		switch (notification.getFeatureID(IntervalIterationBlock.class)) {
			case NablaPackage.INTERVAL_ITERATION_BLOCK__TO_INCLUDED:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case NablaPackage.INTERVAL_ITERATION_BLOCK__INDEX:
			case NablaPackage.INTERVAL_ITERATION_BLOCK__FROM:
			case NablaPackage.INTERVAL_ITERATION_BLOCK__TO:
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
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__INDEX,
				 NablaFactory.eINSTANCE.createSizeTypeSymbol()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__FROM,
				 NablaFactory.eINSTANCE.createSizeType()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__FROM,
				 NablaFactory.eINSTANCE.createSizeTypeOperation()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__FROM,
				 NablaFactory.eINSTANCE.createSizeTypeInt()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__FROM,
				 NablaFactory.eINSTANCE.createSizeTypeSymbolRef()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO,
				 NablaFactory.eINSTANCE.createSizeType()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO,
				 NablaFactory.eINSTANCE.createSizeTypeOperation()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO,
				 NablaFactory.eINSTANCE.createSizeTypeInt()));

		newChildDescriptors.add
			(createChildParameter
				(NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO,
				 NablaFactory.eINSTANCE.createSizeTypeSymbolRef()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__FROM ||
			childFeature == NablaPackage.Literals.INTERVAL_ITERATION_BLOCK__TO;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}