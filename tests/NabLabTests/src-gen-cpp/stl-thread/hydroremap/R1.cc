/* DO NOT EDIT THIS FILE - it is machine generated */

#include "R1.h"
#include <rapidjson/document.h>
#include <rapidjson/istreamwrapper.h>
#include <rapidjson/stringbuffer.h>
#include <rapidjson/writer.h>


/******************** Module definition ********************/

R1::R1(CartesianMesh2D& aMesh)
: mesh(aMesh)
, nbCells(mesh.getNbCells())
, rv3(nbCells)
{
}

R1::~R1()
{
}

void
R1::jsonInit(const char* jsonContent)
{
	rapidjson::Document document;
	assert(!document.Parse(jsonContent).HasParseError());
	assert(document.IsObject());
	const rapidjson::Value::Object& options = document.GetObject();

}

/**
 * Job rj1 called @2.0 in simulate method.
 * In variables: hv1
 * Out variables: hv4
 */
void R1::rj1() noexcept
{
	parallel_exec(nbCells, [&](const size_t& cCells)
	{
		mainModule->hv4[cCells] = mainModule->hv1[cCells] * 2.0;
	});
}

/**
 * Job rj2 called @3.0 in simulate method.
 * In variables: hv4
 * Out variables: rv3
 */
void R1::rj2() noexcept
{
	parallel_exec(nbCells, [&](const size_t& cCells)
	{
		rv3[cCells] = mainModule->hv4[cCells] * 3.0;
	});
}