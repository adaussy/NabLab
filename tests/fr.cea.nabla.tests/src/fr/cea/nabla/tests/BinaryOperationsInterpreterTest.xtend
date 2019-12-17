package fr.cea.nabla.tests

import com.google.inject.Inject
import fr.cea.nabla.ir.interpreter.Context
import fr.cea.nabla.ir.interpreter.ModuleInterpreter
import fr.cea.nabla.ir.interpreter.NV0Bool
import fr.cea.nabla.ir.interpreter.NV0Int
import fr.cea.nabla.ir.interpreter.NV0Real
import fr.cea.nabla.ir.interpreter.NV1Int
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

import static fr.cea.nabla.tests.TestUtils.*

@RunWith(XtextRunner)
@InjectWith(NablaInjectorProvider)
class BinaryOperationsInterpreterTest
{
	@Inject CompilationChainHelper compilationHelper
				
	@Test
	def void testGetValueOfNV0Bool_NV0Bool() 
	{
		val model = TestUtils::testModule 
		+
		'''	
		ℾ b1 = true || false; // -> true
		ℾ b2 = true || true; // -> true
		ℾ b3 = false || false; // -> false

		ℾ b4 = true && false; // -> false
		ℾ b5 = true && true; // -> true
		ℾ b6 = false && false; // -> false

		ℾ b7 = true == false; // -> false
		ℾ b8 = true != false; // -> true
		ℾ b9 = true >= false; // -> true
		ℾ b10 = true <= false; // -> false
		ℾ b11 = true > false; // -> true
		ℾ b12 = true < false; // -> false
		'''
		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val context = new Context(irModule)
		
		assertVariableDefaultValue(irModule, context, "b1", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b2", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b3", new NV0Bool(false))

		assertVariableDefaultValue(irModule, context, "b4", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b5", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b6", new NV0Bool(false))

		assertVariableDefaultValue(irModule, context, "b7", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b8", new NV0Bool(true))	
		assertVariableDefaultValue(irModule, context, "b9", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b10", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b11", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b12", new NV0Bool(false))
	}
	
	@Test
	def void testGetValueOfNV0Int_NV0Int() 
	{
		val model = TestUtils::testModule 
		+
		'''	
		ℾ b1 = 1 == 2; // -> false
		ℾ b2 = 1 == 1; // -> true

		ℾ b3 = 1 != 2; // -> true
		ℾ b4 = 2 != 2; // -> false

		ℾ b5 = 1 >= 2; // -> false
		ℾ b6 = 2 >= 2; // -> true

		ℾ b7 = 1 <= 2; // -> true
		ℾ b8 = 2 <= 2; // -> true

		ℾ b9 = 1 > 2; // -> false
		ℾ b10 = 2 > 1; // -> true

		ℾ b11 = 1 < 2; // -> true
		ℾ b12 = 2 < 1; // -> false

		ℕ n1 = 1 + 2; // -> 3
		ℕ n2 = 2 - 1; // -> 1
		ℕ n3 = 2 * 3; // -> 6
		ℕ n4 = 6 / 3; // -> 2
		ℕ n5 = 7 / 3; // -> 2
		ℕ n6 = 7 % 3; // -> 1
		'''
		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val context = new Context(irModule)
		
		assertVariableDefaultValue(irModule, context, "b1", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b2", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b3", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b4", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b5", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b6", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b7", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b8", new NV0Bool(true))	
		assertVariableDefaultValue(irModule, context, "b9", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b10", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b11", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b12", new NV0Bool(false))
		
		assertVariableDefaultValue(irModule, context, "n1", new NV0Int(3))
		assertVariableDefaultValue(irModule, context, "n2", new NV0Int(1))
		assertVariableDefaultValue(irModule, context, "n3", new NV0Int(6))
		assertVariableDefaultValue(irModule, context, "n4", new NV0Int(2))
		assertVariableDefaultValue(irModule, context, "n5", new NV0Int(2))
		assertVariableDefaultValue(irModule, context, "n6", new NV0Int(1))
	}

	@Test
	def void testGetValueOfNV0Int_NV0Real() 
	{
		val model = TestUtils::testModule 
		+
		'''	
		ℾ b1 = 1 == 2.; // -> false
		ℾ b2 = 1 == 1; // -> true

		ℾ b3 = 1 != 2.; // -> true
		ℾ b4 = 2 != 2.; // -> false

		ℾ b5 = 1 >= 2.; // -> false
		ℾ b6 = 2 >= 2.; // -> true

		ℾ b7 = 1 <= 2.; // -> true
		ℾ b8 = 2 <= 2.; // -> true

		ℾ b9 = 1 > 2.; // -> false
		ℾ b10 = 2 > 1.; // -> true

		ℾ b11 = 1 < 2.; // -> true
		ℾ b12 = 2 < 1.; // -> false

		ℝ n1 = 1 + 2.; // -> 3.
		ℝ n2 = 2 - 1.; // -> 1.
		ℝ n3 = 2 * 3.; // -> 6.
		ℝ n4 = 6 / 3.; // -> 2.
		ℝ n5 = 7 / 2.; // -> 3.5.
		'''
		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val context = new Context(irModule)
		
		assertVariableDefaultValue(irModule, context, "b1", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b2", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b3", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b4", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b5", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b6", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b7", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b8", new NV0Bool(true))	
		assertVariableDefaultValue(irModule, context, "b9", new NV0Bool(false))
		assertVariableDefaultValue(irModule, context, "b10", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b11", new NV0Bool(true))
		assertVariableDefaultValue(irModule, context, "b12", new NV0Bool(false))
		
		assertVariableDefaultValue(irModule, context, "n1", new NV0Real(3.0))
		assertVariableDefaultValue(irModule, context, "n2", new NV0Real(1.0))
		assertVariableDefaultValue(irModule, context, "n3", new NV0Real(6.0))
		assertVariableDefaultValue(irModule, context, "n4", new NV0Real(2.0))
		assertVariableDefaultValue(irModule, context, "n5", new NV0Real(3.5))
	}
	
		@Test
	def void testGetValueOfNV0Int_NV1Int() 
	{
		val model = TestUtils::testModule 
		+
		'''
		ℕ[2] n1 = [1,2];
		ℕ[2] n2 = 3 + n1;
	//	ℕ[2] n3 = 3 * n1;
		'''
		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val moduleInterpreter = new ModuleInterpreter(irModule)
		val context = moduleInterpreter.interprete
		
		assertVariableDefaultValue(irModule, context, "n1", new NV1Int(#[1, 2]))
		assertVariableValueInContext(irModule, context, "n1", new NV1Int(#[1, 2]))
		//assertVariableValueInContext(irModule, context, "n2", new NV1Int(#[4, 5]))
		//assertVariableValueInContext(irModule, context, "n3", new NV1Int(#[3, 6]))
	}
}