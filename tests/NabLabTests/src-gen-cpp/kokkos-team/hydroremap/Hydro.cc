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
		throw std::runtime_error("** Assertion failed");
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

	// maxTime
	if (options.HasMember("maxTime"))
	{
		const rapidjson::Value& valueof_maxTime = options["maxTime"];
		assert(valueof_maxTime.IsDouble());
		maxTime = valueof_maxTime.GetDouble();
	}
	else
	{
		maxTime = 0.1;
	}
	// maxIter
	if (options.HasMember("maxIter"))
	{
		const rapidjson::Value& valueof_maxIter = options["maxIter"];
		assert(valueof_maxIter.IsInt());
		maxIter = valueof_maxIter.GetInt();
	}
	else
	{
		maxIter = 500;
	}
	// deltat
	if (options.HasMember("deltat"))
	{
		const rapidjson::Value& valueof_deltat = options["deltat"];
		assert(valueof_deltat.IsDouble());
		deltat = valueof_deltat.GetDouble();
	}
	else
	{
		deltat = 1.0;
	}

	// Copy node coordinates
	const auto& gNodes = mesh.getGeometry()->getNodes();
	for (size_t rNodes=0; rNodes<nbNodes; rNodes++)
	{
		X(rNodes)[0] = gNodes[rNodes][0];
		X(rNodes)[1] = gNodes[rNodes][1];
	}
}


const std::pair<size_t, size_t> Hydro::computeTeamWorkRange(const member_type& thread, const size_t& nb_elmt) noexcept
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
 * Job iniHv1 called @1.0 in simulate method.
 * In variables: 
 * Out variables: hv1
 */
void Hydro::iniHv1(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			hv1(cCells) = 2.0;
		});
	}
}

/**
 * Job iniHv2 called @1.0 in simulate method.
 * In variables: 
 * Out variables: hv2
 */
void Hydro::iniHv2(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			hv2(cCells) = 0.0;
		});
	}
}

/**
 * Job hj1 called @2.0 in simulate method.
 * In variables: hv2
 * Out variables: hv3
 */
void Hydro::hj1(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			hv3(cCells) = hv2(cCells) + 1.0;
		});
	}
}

/**
 * Job oracleHv1 called @2.0 in simulate method.
 * In variables: hv1
 * Out variables: 
 */
void Hydro::oracleHv1(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv1(hydrofreefuncs::assertEquals(2.0, hv1(cCells)));
		});
	}
}

/**
 * Job oracleHv2 called @2.0 in simulate method.
 * In variables: hv2
 * Out variables: 
 */
void Hydro::oracleHv2(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv2(hydrofreefuncs::assertEquals(0.0, hv2(cCells)));
		});
	}
}

/**
 * Job hj2 called @3.0 in simulate method.
 * In variables: hv3
 * Out variables: hv5
 */
void Hydro::hj2(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			hv5(cCells) = hv3(cCells) + 2.0;
		});
	}
}

/**
 * Job oracleHv3 called @3.0 in simulate method.
 * In variables: hv3
 * Out variables: 
 */
void Hydro::oracleHv3(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv3(hydrofreefuncs::assertEquals(1.0, hv3(cCells)));
		});
	}
}

/**
 * Job oracleHv4 called @3.0 in simulate method.
 * In variables: hv4
 * Out variables: 
 */
void Hydro::oracleHv4(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv4(hydrofreefuncs::assertEquals(4.0, hv4(cCells)));
		});
	}
}

/**
 * Job oracleHv5 called @4.0 in simulate method.
 * In variables: hv5
 * Out variables: 
 */
void Hydro::oracleHv5(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv5(hydrofreefuncs::assertEquals(3.0, hv5(cCells)));
		});
	}
}

/**
 * Job hj3 called @5.0 in simulate method.
 * In variables: hv4, hv5, hv6
 * Out variables: hv7
 */
void Hydro::hj3(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			hv7(cCells) = hv4(cCells) + hv5(cCells) + hv6(cCells);
		});
	}
}

/**
 * Job oracleHv6 called @5.0 in simulate method.
 * In variables: hv6
 * Out variables: 
 */
void Hydro::oracleHv6(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv6(hydrofreefuncs::assertEquals(6.0, hv6(cCells)));
		});
	}
}

/**
 * Job oracleHv7 called @6.0 in simulate method.
 * In variables: hv7
 * Out variables: 
 */
void Hydro::oracleHv7(const member_type& teamMember) noexcept
{
	{
		const auto teamWork(computeTeamWorkRange(teamMember, nbCells));
		if (!teamWork.second)
			return;
	
		Kokkos::parallel_for(Kokkos::TeamThreadRange(teamMember, teamWork.second), KOKKOS_LAMBDA(const size_t& cCellsTeam)
		{
			int cCells(cCellsTeam + teamWork.first);
			const bool testHv7(hydrofreefuncs::assertEquals(13.0, hv7(cCells)));
		});
	}
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
		iniHv1(thread);
		iniHv2(thread);
	});
	
	// @2.0
	Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread)
	{
		hj1(thread);
		oracleHv1(thread);
		oracleHv2(thread);
		r1->rj1(thread);
	});
	
	// @3.0
	Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread)
	{
		hj2(thread);
		oracleHv3(thread);
		oracleHv4(thread);
		r2->rj1(thread);
		r1->rj2(thread);
	});
	
	// @4.0
	Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread)
	{
		oracleHv5(thread);
		r2->rj2(thread);
	});
	
	// @5.0
	Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread)
	{
		hj3(thread);
		oracleHv6(thread);
	});
	
	// @6.0
	Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread)
	{
		oracleHv7(thread);
	});
	
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
	if (d.HasMember("hydro"))
	{
		rapidjson::StringBuffer strbuf;
		rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
		d["hydro"].Accept(writer);
		hydro->jsonInit(strbuf.GetString());
	}
	R1* r1 = new R1(mesh);
	if (d.HasMember("r1"))
	{
		rapidjson::StringBuffer strbuf;
		rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
		d["r1"].Accept(writer);
		r1->jsonInit(strbuf.GetString());
	}
	r1->setMainModule(hydro);
	R2* r2 = new R2(mesh);
	if (d.HasMember("r2"))
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
