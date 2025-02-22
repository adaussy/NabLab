/* DO NOT EDIT THIS FILE - it is machine generated */

#ifndef __HYDRO_H_
#define __HYDRO_H_

#include <fstream>
#include <iomanip>
#include <type_traits>
#include <limits>
#include <utility>
#include <cmath>
#include <rapidjson/document.h>
#include <Kokkos_Core.hpp>
#include <Kokkos_hwloc.hpp>
#include "nablalib/utils/Utils.h"
#include "nablalib/utils/Timer.h"
#include "nablalib/types/Types.h"
#include "nablalib/utils/kokkos/Parallel.h"
#include "CartesianMesh2D.h"

using namespace nablalib::utils;
using namespace nablalib::types;
using namespace nablalib::utils::kokkos;

class R1;
class R2;

/******************** Free functions declarations ********************/

namespace hydrofreefuncs
{
bool assertEquals(double expected, double actual);
}

/******************** Module declaration ********************/

class Hydro
{
	friend class R1;
	friend class R2;

	typedef Kokkos::TeamPolicy<Kokkos::DefaultExecutionSpace::scratch_memory_space>::member_type member_type;

public:
	Hydro(CartesianMesh2D& aMesh);
	~Hydro();

	void jsonInit(const char* jsonContent);

	void simulate();
	void iniHv1(const member_type& teamMember) noexcept;
	void iniHv2(const member_type& teamMember) noexcept;
	void hj1(const member_type& teamMember) noexcept;
	void oracleHv1(const member_type& teamMember) noexcept;
	void oracleHv2(const member_type& teamMember) noexcept;
	void hj2(const member_type& teamMember) noexcept;
	void oracleHv3(const member_type& teamMember) noexcept;
	void oracleHv4(const member_type& teamMember) noexcept;
	void oracleHv5(const member_type& teamMember) noexcept;
	void hj3(const member_type& teamMember) noexcept;
	void oracleHv6(const member_type& teamMember) noexcept;
	void oracleHv7(const member_type& teamMember) noexcept;

private:
	/**
	 * Utility function to get work load for each team of threads
	 * In  : thread and number of element to use for computation
	 * Out : pair of indexes, 1st one for start of chunk, 2nd one for size of chunk
	 */
	const std::pair<size_t, size_t> computeTeamWorkRange(const member_type& thread, const size_t& nb_elmt) noexcept;

	// Mesh and mesh variables
	CartesianMesh2D& mesh;
	size_t nbNodes, nbCells;

	// Additional modules
	R1* r1;
	R2* r2;

	// Options and global variables
	double maxTime;
	int maxIter;
	double deltat;
	static constexpr double t = 0.0;
	Kokkos::View<RealArray1D<2>*> X;
	Kokkos::View<double*> hv1;
	Kokkos::View<double*> hv2;
	Kokkos::View<double*> hv3;
	Kokkos::View<double*> hv4;
	Kokkos::View<double*> hv5;
	Kokkos::View<double*> hv6;
	Kokkos::View<double*> hv7;

	// Timers
	Timer globalTimer;
	Timer cpuTimer;
	Timer ioTimer;
};

#endif
