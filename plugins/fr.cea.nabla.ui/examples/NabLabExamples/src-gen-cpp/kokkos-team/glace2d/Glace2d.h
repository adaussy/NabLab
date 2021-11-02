/* DO NOT EDIT THIS FILE - it is machine generated */

#ifndef __GLACE2D_H_
#define __GLACE2D_H_

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
#include "PvdFileWriter2D.h"

using namespace nablalib::utils;
using namespace nablalib::types;
using namespace nablalib::utils::kokkos;

/******************** Free functions declarations ********************/

namespace glace2dfreefuncs
{
double det(RealArray2D<2,2> a);
RealArray1D<2> perp(RealArray1D<2> a);
template<size_t x>
double dot(RealArray1D<x> a, RealArray1D<x> b);
template<size_t x>
double norm(RealArray1D<x> a);
template<size_t l>
RealArray2D<l,l> tensProduct(RealArray1D<l> a, RealArray1D<l> b);
template<size_t x, size_t y>
RealArray1D<x> matVectProduct(RealArray2D<x,y> a, RealArray1D<y> b);
template<size_t l>
double trace(RealArray2D<l,l> a);
RealArray2D<2,2> inverse(RealArray2D<2,2> a);
template<size_t x>
RealArray1D<x> sumR1(RealArray1D<x> a, RealArray1D<x> b);
double sumR0(double a, double b);
template<size_t x>
RealArray2D<x,x> sumR2(RealArray2D<x,x> a, RealArray2D<x,x> b);
double minR0(double a, double b);
}

/******************** Module declaration ********************/

class Glace2d
{
	typedef Kokkos::TeamPolicy<Kokkos::DefaultExecutionSpace::scratch_memory_space>::member_type member_type;

public:
	Glace2d(CartesianMesh2D& aMesh);
	~Glace2d();

	void jsonInit(const char* jsonContent);

	void simulate();
	void computeCjr(const member_type& teamMember) noexcept;
	void computeInternalEnergy(const member_type& teamMember) noexcept;
	void iniCjrIc(const member_type& teamMember) noexcept;
	void iniTime() noexcept;
	void computeLjr(const member_type& teamMember) noexcept;
	void computeV(const member_type& teamMember) noexcept;
	void initialize(const member_type& teamMember) noexcept;
	void setUpTimeLoopN(const member_type& teamMember) noexcept;
	void computeDensity(const member_type& teamMember) noexcept;
	void executeTimeLoopN() noexcept;
	void computeEOSp(const member_type& teamMember) noexcept;
	void computeEOSc(const member_type& teamMember) noexcept;
	void computeAjr(const member_type& teamMember) noexcept;
	void computedeltatj(const member_type& teamMember) noexcept;
	void computeAr(const member_type& teamMember) noexcept;
	void computeBr(const member_type& teamMember) noexcept;
	void computeDt(const member_type& teamMember) noexcept;
	void computeBoundaryConditions(const member_type& teamMember) noexcept;
	void computeBt(const member_type& teamMember) noexcept;
	void computeMt(const member_type& teamMember) noexcept;
	void computeTn() noexcept;
	void computeU(const member_type& teamMember) noexcept;
	void computeFjr(const member_type& teamMember) noexcept;
	void computeXn(const member_type& teamMember) noexcept;
	void computeEn(const member_type& teamMember) noexcept;
	void computeUn(const member_type& teamMember) noexcept;

private:
	void dumpVariables(int iteration, bool useTimer=true);

	/**
	 * Utility function to get work load for each team of threads
	 * In  : thread and number of element to use for computation
	 * Out : pair of indexes, 1st one for start of chunk, 2nd one for size of chunk
	 */
	const std::pair<size_t, size_t> computeTeamWorkRange(const member_type& thread, const size_t& nb_elmt) noexcept;

	// Mesh and mesh variables
	CartesianMesh2D& mesh;
	size_t nbNodes, nbCells, maxNodesOfCell, maxCellsOfNode, nbInnerNodes, nbTopNodes, nbBottomNodes, nbLeftNodes, nbRightNodes;

	// Options and global variables
	PvdFileWriter2D* writer;
	std::string outputPath;
	int outputPeriod;
	int lastDump;
	int n;
	double stopTime;
	int maxIterations;
	double gamma;
	double xInterface;
	double deltatCfl;
	double rhoIniZg;
	double rhoIniZd;
	double pIniZg;
	double pIniZd;
	double t_n;
	double t_nplus1;
	double t_n0;
	double deltat;
	Kokkos::View<RealArray1D<2>*> X_n;
	Kokkos::View<RealArray1D<2>*> X_nplus1;
	Kokkos::View<RealArray1D<2>*> X_n0;
	Kokkos::View<RealArray1D<2>*> b;
	Kokkos::View<RealArray1D<2>*> bt;
	Kokkos::View<RealArray2D<2,2>*> Ar;
	Kokkos::View<RealArray2D<2,2>*> Mt;
	Kokkos::View<RealArray1D<2>*> ur;
	Kokkos::View<double*> c;
	Kokkos::View<double*> m;
	Kokkos::View<double*> p;
	Kokkos::View<double*> rho;
	Kokkos::View<double*> e;
	Kokkos::View<double*> E_n;
	Kokkos::View<double*> E_nplus1;
	Kokkos::View<double*> V;
	Kokkos::View<double*> deltatj;
	Kokkos::View<RealArray1D<2>*> uj_n;
	Kokkos::View<RealArray1D<2>*> uj_nplus1;
	Kokkos::View<double**> l;
	Kokkos::View<RealArray1D<2>**> Cjr_ic;
	Kokkos::View<RealArray1D<2>**> C;
	Kokkos::View<RealArray1D<2>**> F;
	Kokkos::View<RealArray2D<2,2>**> Ajr;

	// Timers
	Timer globalTimer;
	Timer cpuTimer;
	Timer ioTimer;
};

#endif
