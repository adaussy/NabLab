/* DO NOT EDIT THIS FILE - it is machine generated */

#include "Hydro.h"
#include <rapidjson/document.h>
#include <rapidjson/istreamwrapper.h>
#include <rapidjson/stringbuffer.h>
#include <rapidjson/writer.h>
#include "R1.h"
#include "R2.h"


/******************** Free functions definitions ********************/

namespace hydrofreefuncs
{
bool assertEquals(double expected, double actual)
{
	const bool ret((expected == actual));
	if (!ret) 
	{
		throw std::runtime_error("** Assertion failed");
	}
	return ret;
}
}

/******************** Module definition ********************/

Hydro::Hydro(CartesianMesh2D& aMesh)
: mesh(aMesh)
, nbNodes(mesh.getNbNodes())
, nbCells(mesh.getNbCells())
, X("X", nbNodes)
, hv1("hv1", nbCells)
, hv2("hv2", nbCells)
, hv3("hv3", nbCells)
, hv4("hv4", nbCells)
, hv5("hv5", nbCells)
, hv6("hv6", nbCells)
, hv7("hv7", nbCells)
{
}

Hydro::~Hydro()
{
}

void
Hydro::jsonInit(const char* jsonContent)
{
	rapidjson::Document document;
	assert(!document.Parse(jsonContent).HasParseError());
	assert(document.IsObject());
	const rapidjson::Value::Object& options = document.GetObject();

	assert(options.HasMember("maxIter"));
	const rapidjson::Value& valueof_maxIter = options["maxIter"];
	assert(valueof_maxIter.IsInt());
	maxIter = valueof_maxIter.GetInt();
	assert(options.HasMember("maxTime"));
	const rapidjson::Value& valueof_maxTime = options["maxTime"];
	assert(valueof_maxTime.IsDouble());
	maxTime = valueof_maxTime.GetDouble();
	assert(options.HasMember("deltat"));
	const rapidjson::Value& valueof_deltat = options["deltat"];
	assert(valueof_deltat.IsDouble());
	deltat = valueof_deltat.GetDouble();

	// Copy node coordinates
	const auto& gNodes = mesh.getGeometry()->getNodes();
	for (size_t rNodes=0; rNodes<nbNodes; rNodes++)
	{
		X(rNodes)[0] = gNodes[rNodes][0];
		X(rNodes)[1] = gNodes[rNodes][1];
	}
}

/**
 * Job iniHv1 called @1.0 in simulate method.
 * In variables: 
 * Out variables: hv1
 */
void Hydro::iniHv1() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		hv1(cCells) = 2.0;
	});
}

/**
 * Job iniHv2 called @1.0 in simulate method.
 * In variables: 
 * Out variables: hv2
 */
void Hydro::iniHv2() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		hv2(cCells) = 0.0;
	});
}

/**
 * Job hj1 called @2.0 in simulate method.
 * In variables: hv2
 * Out variables: hv3
 */
void Hydro::hj1() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		hv3(cCells) = hv2(cCells) + 1.0;
	});
}

/**
 * Job oracleHv1 called @2.0 in simulate method.
 * In variables: hv1
 * Out variables: 
 */
void Hydro::oracleHv1() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv1(hydrofreefuncs::assertEquals(2.0, hv1(cCells)));
	});
}

/**
 * Job oracleHv2 called @2.0 in simulate method.
 * In variables: hv2
 * Out variables: 
 */
void Hydro::oracleHv2() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv2(hydrofreefuncs::assertEquals(0.0, hv2(cCells)));
	});
}

/**
 * Job hj2 called @3.0 in simulate method.
 * In variables: hv3
 * Out variables: hv5
 */
void Hydro::hj2() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		hv5(cCells) = hv3(cCells) + 2.0;
	});
}

/**
 * Job oracleHv3 called @3.0 in simulate method.
 * In variables: hv3
 * Out variables: 
 */
void Hydro::oracleHv3() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv3(hydrofreefuncs::assertEquals(1.0, hv3(cCells)));
	});
}

/**
 * Job oracleHv4 called @3.0 in simulate method.
 * In variables: hv4
 * Out variables: 
 */
void Hydro::oracleHv4() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv4(hydrofreefuncs::assertEquals(4.0, hv4(cCells)));
	});
}

/**
 * Job oracleHv5 called @4.0 in simulate method.
 * In variables: hv5
 * Out variables: 
 */
void Hydro::oracleHv5() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv5(hydrofreefuncs::assertEquals(3.0, hv5(cCells)));
	});
}

/**
 * Job hj3 called @5.0 in simulate method.
 * In variables: hv4, hv5, hv6
 * Out variables: hv7
 */
void Hydro::hj3() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		hv7(cCells) = hv4(cCells) + hv5(cCells) + hv6(cCells);
	});
}

/**
 * Job oracleHv6 called @5.0 in simulate method.
 * In variables: hv6
 * Out variables: 
 */
void Hydro::oracleHv6() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv6(hydrofreefuncs::assertEquals(6.0, hv6(cCells)));
	});
}

/**
 * Job oracleHv7 called @6.0 in simulate method.
 * In variables: hv7
 * Out variables: 
 */
void Hydro::oracleHv7() noexcept
{
	Kokkos::parallel_for(nbCells, KOKKOS_LAMBDA(const size_t& cCells)
	{
		const bool testHv7(hydrofreefuncs::assertEquals(13.0, hv7(cCells)));
	});
}

void Hydro::simulate()
{
	std::cout << "\n" << __BLUE_BKG__ << __YELLOW__ << __BOLD__ <<"\tStarting HydroRemap ..." << __RESET__ << "\n\n";
	
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

	iniHv1(); // @1.0
	iniHv2(); // @1.0
	hj1(); // @2.0
	oracleHv1(); // @2.0
	oracleHv2(); // @2.0
	r1->rj1(); // @2.0
	hj2(); // @3.0
	oracleHv3(); // @3.0
	oracleHv4(); // @3.0
	r2->rj1(); // @3.0
	r1->rj2(); // @3.0
	oracleHv5(); // @4.0
	r2->rj2(); // @4.0
	hj3(); // @5.0
	oracleHv6(); // @5.0
	oracleHv7(); // @6.0
	
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
		std::cerr << "(HydroRemap.json)" << std::endl;
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
	Hydro* hydro = new Hydro(mesh);
	assert(d.HasMember("hydro"));
	{
		rapidjson::StringBuffer strbuf;
		rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
		d["hydro"].Accept(writer);
		hydro->jsonInit(strbuf.GetString());
	}
	R1* r1 = new R1(mesh);
	assert(d.HasMember("r1"));
	{
		rapidjson::StringBuffer strbuf;
		rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
		d["r1"].Accept(writer);
		r1->jsonInit(strbuf.GetString());
	}
	r1->setMainModule(hydro);
	R2* r2 = new R2(mesh);
	assert(d.HasMember("r2"));
	{
		rapidjson::StringBuffer strbuf;
		rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
		d["r2"].Accept(writer);
		r2->jsonInit(strbuf.GetString());
	}
	r2->setMainModule(hydro);
	
	// Start simulation
	// Simulator must be a pointer when a finalize is needed at the end (Kokkos, omp...)
	hydro->simulate();
	
	delete r2;
	delete r1;
	delete hydro;
	// simulator must be deleted before calling finalize
	Kokkos::finalize();
	return ret;
}
