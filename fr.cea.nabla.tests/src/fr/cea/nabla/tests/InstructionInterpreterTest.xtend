package fr.cea.nabla.tests

import com.google.inject.Inject
import fr.cea.nabla.ir.interpreter.ModuleInterpreter
import fr.cea.nabla.ir.interpreter.NV0Real
import fr.cea.nabla.ir.interpreter.NV1Real
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

import static fr.cea.nabla.tests.TestUtils.*

@RunWith(XtextRunner)
@InjectWith(NablaInjectorProvider)
class InstructionInterpreterTest 
{
	@Inject CompilationChainHelper compilationHelper

	@Test
	def void testInterpreteVarDefinition()
	{
		val model = TestUtils::testModule
		+
		'''
		Job1: { ℝ r = 1.0; t = r; }
		'''

		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val moduleInterpreter = new ModuleInterpreter(irModule)
		val context = moduleInterpreter.interprete

		assertVariableValueInContext(irModule, context, "t", new NV0Real(1.0))
	}

	@Test
	def void testInterpreteInstructionBlock()
	{
		val model = TestUtils::testModule
		+
		'''
		Job1: { ℝ r = 1.0; t = r; }
		'''

		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val moduleInterpreter = new ModuleInterpreter(irModule)
		val context = moduleInterpreter.interprete

		assertVariableValueInContext(irModule, context, "t", new NV0Real(1.0))
	}

	@Test
	def void testInterpreteAffectation()
	{
		val model = TestUtils::testModule
		+
		'''
		Job1: { ℝ r = 1.0; t = r; }
		'''

		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val moduleInterpreter = new ModuleInterpreter(irModule)
		val context = moduleInterpreter.interprete

		assertVariableValueInContext(irModule, context, "t", new NV0Real(1.0))
	}

	@Test
	def void testInterpreteLoop()
	{
		val xQuads = 10
		val yQuads = 10
		val model = TestUtils::getTestModule(xQuads, yQuads, 0.2, 1)
		+
		'''
		ℝ U{cells};
		ℝ r;
		InitU : ∀r∈cells(), U{r} = 1.0;
		'''

		val irModule = compilationHelper.getIrModule(model, TestUtils::testGenModel)
		val moduleInterpreter = new ModuleInterpreter(irModule)
		val context = moduleInterpreter.interprete

		val double[] res = newDoubleArrayOfSize(xQuads * yQuads)
		for (var i = 0 ; i < res.length ; i++)
			res.set(i, 1.0)

		assertVariableValueInContext(irModule, context, "U", new NV1Real(res))
	}
}