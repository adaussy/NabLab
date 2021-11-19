/* DO NOT EDIT THIS FILE - it is machine generated */

#include "Variables.h"
#include <rapidjson/document.h>
#include <rapidjson/istreamwrapper.h>
#include <rapidjson/stringbuffer.h>
#include <rapidjson/writer.h>


/******************** Free functions definitions ********************/

namespace variablesfreefuncs
{
bool assertEquals(int expected, int actual)
{
	const bool ret((expected == actual));
	if (!ret) 
		throw std::runtime_error("** Assertion failed");
	return ret;
}

template<size_t x>
bool assertEquals(RealArray1D<x> expected, RealArray1D<x> actual)
{
	for (size_t i=0; i<x; i++)
	{
		if (expected[i] != actual[i]) 
			throw std::runtime_error("** Assertion failed");
	}
	return true;
}

template<size_t x>
bool assertEquals(IntArray1D<x> expected, IntArray1D<x> actual)
{
	for (size_t i=0; i<x; i++)
	{
		if (expected[i] != actual[i]) 
			throw std::runtime_error("** Assertion failed");
	}
	return true;
}

template<size_t x0>
RealArray1D<x0> operator+(RealArray1D<x0> a, RealArray1D<x0> b)
{
	RealArray1D<x0> result;
	for (size_t ix0=0; ix0<x0; ix0++)
	{
		result[ix0] = a[ix0] + b[ix0];
	}
	return result;
}
}

/******************** Module definition ********************/

Variables::Variables(CartesianMesh2D& aMesh)
: mesh(aMesh)
, nbNodes(mesh.getNbNodes())
, X("X", nbNodes)
{
}

Variables::~Variables()
{
}

void
Variables::jsonInit(const char* jsonContent)
{
	rapidjson::Document document;
	assert(!document.Parse(jsonContent).HasParseError());
	assert(document.IsObject());
	const rapidjson::Value::Object& options = document.GetObject();

	// optDim
	if (options.HasMember("optDim"))
	{
		const rapidjson::Value& valueof_optDim = options["optDim"];
		assert(valueof_optDim.IsInt());
		optDim = valueof_optDim.GetInt();
	}
	else
	{
		optDim = 2;
	}
	// optVect1
	if (options.HasMember("optVect1"))
	{
		const rapidjson::Value& valueof_optVect1 = options["optVect1"];
		assert(valueof_optVect1.IsArray());
		assert(valueof_optVect1.Size() == 2);
		for (size_t i1=0 ; i1<2 ; i1++)
		{
			assert(valueof_optVect1[i1].IsDouble());
			optVect1[i1] = valueof_optVect1[i1].GetDouble();
		}
	}
	else
	{
		optVect1 = {1.0, 1.0};
	}
	// optVect2
	if (options.HasMember("optVect2"))
	{
		const rapidjson::Value& valueof_optVect2 = options["optVect2"];
		assert(valueof_optVect2.IsArray());
		assert(valueof_optVect2.Size() == 2);
		for (size_t i1=0 ; i1<2 ; i1++)
		{
			assert(valueof_optVect2[i1].IsDouble());
			optVect2[i1] = valueof_optVect2[i1].GetDouble();
		}
	}
	else
	{
		optVect2 = {1.0, 1.0};
	}
	optVect3 = variablesfreefuncs::operator+(optVect1, optVect2);
	// mandatoryOptDim
	assert(options.HasMember("mandatoryOptDim"));
	const rapidjson::Value& valueof_mandatoryOptDim = options["mandatoryOptDim"];
	assert(valueof_mandatoryOptDim.IsInt());
	mandatoryOptDim = valueof_mandatoryOptDim.GetInt();
	// mandatoryOptVect
	assert(options.HasMember("mandatoryOptVect"));
	const rapidjson::Value& valueof_mandatoryOptVect = options["mandatoryOptVect"];
	assert(valueof_mandatoryOptVect.IsArray());
	assert(valueof_mandatoryOptVect.Size() == 2);
	for (size_t i1=0 ; i1<2 ; i1++)
	{
		assert(valueof_mandatoryOptVect[i1].IsInt());
		mandatoryOptVect[i1] = valueof_mandatoryOptVect[i1].GetInt();
	}
	varVec = {1.0, 1.0};
	dynamicVec.initSize(optDim);

	// Copy node coordinates
	const auto& gNodes = mesh.getGeometry()->getNodes();
	for (size_t rNodes=0; rNodes<nbNodes; rNodes++)
	{
		X(rNodes)[0] = gNodes[rNodes][0];
		X(rNodes)[1] = gNodes[rNodes][1];
	}
}


const std::pair<size_t, size_t> Variables::computeTeamWorkRange(const member_type& thread, const size_t& nb_elmt) noexcept
{
	/*
	if (nb_elmt % thread.team_size())
	{
		std::cerr << "[ERROR] nb of elmt (" << nb_elmt << ") not multiple of nb of thread per team ("
	              << thread.team_size() << ")" << std::endl;
		std::terminate();
	}
	*/
	// Size
	size_t team_chunk(std::floor(nb_elmt / thread.league_size()));
	// Offset
	const size_t team_offset(thread.league_rank() * team_chunk);
	// Last team get remaining work
	if (thread.league_rank() == thread.league_size() - 1)
	{
		size_t left_over(nb_elmt - (team_chunk * thread.league_size()));
		team_chunk += left_over;
	}
	return std::pair<size_t, size_t>(team_offset, team_chunk);
}

/**
 * Job dynamicVecInitialization called @1.0 in simulate method.
 * In variables: optDim
 * Out variables: checkDynamicDim, dynamicVec
 */
void Variables::dynamicVecInitialization(const member_type& teamMember) noexcept
{
	int cpt(0);
	for (size_t i=0; i<optDim; i++)
	{
		cpt = cpt + 1;
		dynamicVec[i] = 3.3;
	}
	checkDynamicDim = cpt;
}

/**
 * Job varVecInitialization called @1.0 in simulate method.
 * In variables: constexprDim
 * Out variables: varVec
 */
void Variables::varVecInitialization() noexcept
{
	varVec = {2.2, 2.2};
}

/**
 * Job oracle called @2.0 in simulate method.
 * In variables: checkDynamicDim, constexprDim, constexprVec, mandatoryOptDim, mandatoryOptVect, optDim, optVect1, optVect2, optVect3, varVec
 * Out variables: 
 */
void Variables::oracle() noexcept
{
	const bool testOptDim(variablesfreefuncs::assertEquals(2, optDim));
	const bool testOptVect1(variablesfreefuncs::assertEquals({1.0, 1.0}, optVect1));
	const bool testOptVect2(variablesfreefuncs::assertEquals({2.0, 2.0}, optVect2));
	const bool testOptVect3(variablesfreefuncs::assertEquals({3.0, 3.0}, optVect3));
	const bool testMandatoryOptDim(variablesfreefuncs::assertEquals(3, mandatoryOptDim));
	const bool testMandatoryOptVect(variablesfreefuncs::assertEquals({3, 3}, mandatoryOptVect));
	const bool testConstexprDim(variablesfreefuncs::assertEquals(2, constexprDim));
	const bool testConstexprVec(variablesfreefuncs::assertEquals({1.1, 1.1}, constexprVec));
	const bool testVarVec(variablesfreefuncs::assertEquals({2.2, 2.2}, varVec));
	const bool testDynamicVecLength(variablesfreefuncs::assertEquals(2, checkDynamicDim));
}

void Variables::simulate()
{
	std::cout << "\n" << __BLUE_BKG__ << __YELLOW__ << __BOLD__ <<"\tStarting Variables ..." << __RESET__ << "\n\n";
	
	if (Kokkos::hwloc::available())
	{
		std::cout << "[" << __GREEN__ << "TOPOLOGY" << __RESET__ << "]  NUMA=" << __BOLD__ << Kokkos::hwloc::get_available_numa_count()
			<< __RESET__ << ", Cores/NUMA=" << __BOLD__ << Kokkos::hwloc::get_available_cores_per_numa()
			<< __RESET__ << ", Threads/Core=" << __BOLD__ << Kokkos::hwloc::get_available_threads_per_core() << __RESET__ << std::endl;
	}
	else
	{
		std::cout << "[" << __GREEN__ << "TOPOLOGY" << __RESET__ << "]  HWLOC unavailable cannot get topological informations" << std::endl;
	}
	
	// std::cout << "[" << __GREEN__ << "KOKKOS" << __RESET__ << "]    " << __BOLD__ << (is_same<MyLayout,Kokkos::LayoutLeft>::value?"Left":"Right")" << __RESET__ << " layout" << std::endl;
	
	std::cout << "[" << __GREEN__ << "OUTPUT" << __RESET__ << "]    " << __BOLD__ << "Disabled" << __RESET__ << std::endl;

	auto team_policy(Kokkos::TeamPolicy<>(
		Kokkos::hwloc::get_available_numa_count(),
		Kokkos::hwloc::get_available_cores_per_numa() * Kokkos::hwloc::get_available_threads_per_core()));
	
	// @1.0
	Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread)
	{
		if (thread.league_rank() == 0)
			Kokkos::single(Kokkos::PerTeam(thread), KOKKOS_LAMBDA(){
				std::cout << "[" << __GREEN__ << "RUNTIME" << __RESET__ << "]   Using " << __BOLD__ << setw(3) << thread.league_size() << __RESET__ << " team(s) of "
					<< __BOLD__ << setw(3) << thread.team_size() << __RESET__<< " thread(s)" << std::endl;});
		dynamicVecInitialization(thread);
		if (thread.league_rank() == 0)
			Kokkos::single(Kokkos::PerTeam(thread), KOKKOS_LAMBDA(){varVecInitialization();});
	});
	
	// @2.0
	oracle();
	
	std::cout << "\nFinal time = " << t << endl;
	std::cout << __YELLOW__ << "\n\tDone ! Took " << __MAGENTA__ << __BOLD__ << globalTimer.print() << __RESET__ << std::endl;
}

int main(int argc, char* argv[]) 
{
	Kokkos::initialize(argc, argv);
	string dataFile;
	int ret = 0;
	
	if (argc == 2)
	{
		dataFile = argv[1];
	}
	else
	{
		std::cerr << "[ERROR] Wrong number of arguments. Expecting 1 arg: dataFile." << std::endl;
		std::cerr << "(Variables.json)" << std::endl;
		return -1;
	}
	
	// read json dataFile
	ifstream ifs(dataFile);
	rapidjson::IStreamWrapper isw(ifs);
	rapidjson::Document d;
	d.ParseStream(isw);
	assert(d.IsObject());
	
	// Mesh instanciation
	CartesianMesh2D mesh;
	assert(d.HasMember("mesh"));
	rapidjson::StringBuffer strbuf;
	rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
	d["mesh"].Accept(writer);
	mesh.jsonInit(strbuf.GetString());
	
	// Module instanciation(s)
	Variables* variables = new Variables(mesh);
	if (d.HasMember("variables"))
	{
		rapidjson::StringBuffer strbuf;
		rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
		d["variables"].Accept(writer);
		variables->jsonInit(strbuf.GetString());
	}
	
	// Start simulation
	// Simulator must be a pointer when a finalize is needed at the end (Kokkos, omp...)
	variables->simulate();
	
	delete variables;
	// simulator must be deleted before calling finalize
	Kokkos::finalize();
	return ret;
}
