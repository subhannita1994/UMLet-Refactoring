package padl.creator.cppfile.eclipse.test.big;

import java.util.ArrayList;
import padl.cpp.kernel.ICPPClass;
import padl.cpp.kernel.ICPPGhost;
import padl.cpp.kernel.ICPPMemberClass;
import padl.cpp.kernel.ICPPMemberGhost;
import padl.cpp.kernel.IDestructor;
import padl.cpp.kernel.IEnum;
import padl.cpp.kernel.IEnumValue;
import padl.cpp.kernel.IGlobalField;
import padl.cpp.kernel.IGlobalFunction;
import padl.cpp.kernel.IMemberStructure;
import padl.cpp.kernel.IStructure;
import padl.cpp.kernel.IUnion;
import padl.cpp.visitor.ICPPWalker;
import padl.kernel.IAbstractModel;
import padl.kernel.IAggregation;
import padl.kernel.IAssociation;
import padl.kernel.IClass;
import padl.kernel.IComposition;
import padl.kernel.IConstituent;
import padl.kernel.IConstructor;
import padl.kernel.IContainerAggregation;
import padl.kernel.IContainerComposition;
import padl.kernel.ICreation;
import padl.kernel.IDelegatingMethod;
import padl.kernel.IField;
import padl.kernel.IGetter;
import padl.kernel.IGhost;
import padl.kernel.IInterface;
import padl.kernel.IMemberClass;
import padl.kernel.IMemberGhost;
import padl.kernel.IMemberInterface;
import padl.kernel.IMethod;
import padl.kernel.IMethodInvocation;
import padl.kernel.IPackage;
import padl.kernel.IPackageDefault;
import padl.kernel.IParameter;
import padl.kernel.IPrimitiveEntity;
import padl.kernel.ISetter;
import padl.kernel.IUseRelationship;

public class JNICollecteFctGlobaleVisitor2 implements ICPPWalker {
	final ArrayList<String> listOfJNIMethods = new ArrayList<String>();
	private IMethod currentMethod;
	private IClass currentclass;
	private ICPPClass currentcppclass;
	

	@Override
	public Object getResult() {
		return (this.currentclass);
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public void open(IClass aClass) {
		// TODO Auto-generated method stub
		this.currentclass = aClass;
	}

	@Override
	public void open(ICPPClass aCPPClass) {
		// TODO Auto-generated method stub
		this.currentcppclass=aCPPClass;
	}

	@Override
	public void open(IMethod aMethod) {

		this.currentMethod = aMethod;
		System.out.println(aMethod.getDisplayName());
		
		
	}

	// ********************** NOT USED YET ***********************
	@Override
	public void close(IAbstractModel anAbstractModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IClass aClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IConstructor aConstructor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IDelegatingMethod aDelegatingMethod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IGetter aGetter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IGhost aGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IInterface anInterface) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IMemberClass aMemberClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IMemberGhost aMemberGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IMemberInterface aMemberInterface) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IMethod aMethod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IPackage aPackage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IPackageDefault aPackage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(ISetter aSetter) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void open(IAbstractModel anAbstractModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IConstructor aConstructor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IDelegatingMethod aDelegatingMethod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IGetter aGetter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IGhost aGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IInterface anInterface) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IMemberClass aMemberClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IMemberGhost aMemberGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IMemberInterface aMemberInterface) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IPackage aPackage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IPackageDefault aPackage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(ISetter aSetter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unknownConstituentHandler(String aCalledMethodName, IConstituent aConstituent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IAggregation anAggregation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IAssociation anAssociation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IComposition aComposition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IContainerAggregation aContainerAggregation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IContainerComposition aContainerComposition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ICreation aCreation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IField aField) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IMethodInvocation aMethodInvocation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IParameter aParameter) {
		// TODO Auto-generated method stub
		// ArrayList<String> ListeMethodJNI = new ArrayList<String>();
		// System.out.println(aParameter.getDisplayTypeName()+aParameter.getDisplayName());
		
	
	}

	@Override
	public void visit(IPrimitiveEntity aPrimitiveEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IUseRelationship aUse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(ICPPClass aCPPClass) {
		// TODO Auto-generated method stub
		//this.currentcppclass = aCPPClass;
	}

	@Override
	public void close(ICPPGhost aCPPGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(ICPPMemberClass aCPPMemberClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(ICPPMemberGhost aCPPMemberGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IDestructor aDestructor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IEnum anEnum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IGlobalField aGlobalField) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IGlobalFunction aGlobalFunction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IMemberStructure aMemberStructure) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IStructure aStruct) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IUnion anUnion) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void open(ICPPGhost aCPPGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(ICPPMemberClass aCPPMemberClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(ICPPMemberGhost aCPPMemberGhost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IDestructor aDestructor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IEnum anEnum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IGlobalField aGlobalField) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IGlobalFunction aGlobalFunction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IMemberStructure aMemberStructure) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IStructure aStruct) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IUnion anUnion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IEnumValue anEnumValue) {
		// TODO Auto-generated method stub

	}

	

}
