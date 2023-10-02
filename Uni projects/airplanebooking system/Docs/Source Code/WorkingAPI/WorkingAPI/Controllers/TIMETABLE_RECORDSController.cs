using Oracle.ManagedDataAccess.Client;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using WorkingAPI.Models;

namespace WorkingAPI.Controllers
{
    public class TIMETABLE_RECORDSController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/TIMETABLE_RECORDS
        public IQueryable<TIMETABLE_RECORDS> GetTIMETABLE_RECORDS()
        {
            return db.TIMETABLE_RECORDS;
        }

        // GET: api/TIMETABLE_RECORDS/5
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> GetTIMETABLE_RECORDS(decimal id)
        {
            TIMETABLE_RECORDS tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.FindAsync(id);
            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(tIMETABLE_RECORDS);
        }

        [Route("api/TIMETABLE_RECORDS/STAFF")]
        // GET: api/TIMETABLE_RECORDS/STAFF?staffNo={STAFF_ACCOUNT_NO}
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> GetStaffTimetable(decimal staffNo)
        {
            // Select query without the parameters needed for a WHERE statement.

            string queryString;
            queryString =
                "SELECT " +
                "t.JOURNEY_NO, " +
                "t.DEPARTURE_STATION, " +
                "t.ARRIVAL_STATION, " +
                "t.DEPARTURE_TIME, " +
                "t.ARRIVAL_TIME, " +
                "t.JOURNEY_COMMENTS, " +
                "t.JOURNEY_PRICE " +
                "FROM PRCS251J.TIMETABLE_RECORDS t " +
                "INNER JOIN PRCS251J.STAFF_ASSIGNMENTS s ON s.JOURNEY_ID = t.JOURNEY_NO ";

            List<OracleParameter> parameterList;
            parameterList = new List<OracleParameter>();

            // Uses OracleParameter objects to parameterise queries to prevent SQL injection attacks

            queryString += $"WHERE s.STAFF_ACCOUNT_ID = :staffNo";

            OracleParameter parameter;
            parameter = new OracleParameter("staffNo", staffNo);
            parameterList.Add(parameter);

            // Submit query to database and return a list of results as a response.

            OracleParameter[] parameterArray;
            parameterArray = parameterList.ToArray();

            List<TIMETABLE_RECORDS> tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.SqlQuery(queryString, parameterArray).ToListAsync();

            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(tIMETABLE_RECORDS);
        }

        [Route("api/TIMETABLE_RECORDS/search")]
        // GET: api/TIMETABLE_RECORDS?journeyNo={Journey_No}&departureStation={Departure_Station}&arrivalStation={Arrival_Station}
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> GetTIMETABLE_RECORDS(decimal? journeyNo, string departureStation, string arrivalStation, string departureTime, string arrivalTime)
        {
            // Initial query. If no parameters have data, the query returns all data so that the search field narrows as parameters are entered.

            string queryString;
            queryString = "SELECT * FROM PRCS251J.TIMETABLE_RECORDS ";

            List<OracleParameter> parameterList;
            parameterList = new List<OracleParameter>();

            // Conditional statement to concatenate additional items to the query based on what parameters are received with data.
            // Uses OracleParameter objects to parameterise queries to prevent SQL injection attacks

            if (journeyNo == null)
            {
                queryString += "WHERE JOURNEY_NO LIKE '%%' ";
            }
            else
            {
                queryString += $"WHERE JOURNEY_NO LIKE '%' || :journeyNo || '%' ";

                OracleParameter parameter;
                parameter = new OracleParameter("journeyNo", journeyNo);

                parameterList.Add(parameter);
            }

            if (departureStation != null)
            {
                queryString += $"AND LOWER(DEPARTURE_STATION) LIKE '%' || LOWER(:departureStation) || '%'";

                OracleParameter parameter;
                parameter = new OracleParameter("departureStation", departureStation);

                parameterList.Add(parameter);
            }

            if (arrivalStation != null)
            {
                queryString += $"AND LOWER(ARRIVAL_STATION) LIKE '%' || LOWER(:arrivalStation) || '%' ";

                OracleParameter parameter;
                parameter = new OracleParameter("arrivalStation", arrivalStation);

                parameterList.Add(parameter);
            }

            if (departureTime != null)
            {
                queryString += $"AND DEPARTURE_TIME > TO_DATE( :departureTime || ':00', 'dd-mm-yyyy hh24:mi:ss') ";

                OracleParameter parameter;
                parameter = new OracleParameter("departureTime", departureTime);

                parameterList.Add(parameter);
            }

            if (arrivalTime != null)
            {
                queryString += $"AND ARRIVAL_TIME < TO_DATE( :arrivalTime || ':59', 'dd-mm-yyyy hh24:mi:ss') ";

                OracleParameter parameter;
                parameter = new OracleParameter("arrivalTime", arrivalTime);

                parameterList.Add(parameter);
            }

            // Submit query to database and return a list of results as a response.

            OracleParameter[] parameterArray;
            parameterArray = parameterList.ToArray();

            List<TIMETABLE_RECORDS> tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.SqlQuery(queryString, parameterArray).ToListAsync();

            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(tIMETABLE_RECORDS);
        }

        [Route("api/TIMETABLE_RECORDS/test")]
        // GET: api/TIMETABLE_RECORDS?journeyNo={Journey_No}&departureStation={Departure_Station}&arrivalStation={Arrival_Station}
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> GetTIMETABLE_RECORDS(decimal? journeyNo)
        {
            // Initial query. If no parameters have data, the query returns all data so that the search field narrows as parameters are entered.

            string queryString;
            queryString = "SELECT * FROM PRCS251J.TIMETABLE_RECORDS WHERE JOURNEY_NO = ':journeyNo'";

            Oracle.ManagedDataAccess.Client.OracleParameter parameter;
            parameter = new Oracle.ManagedDataAccess.Client.OracleParameter("journeyNo", journeyNo);

            // Submit query to database and return a list of results as a response.

            List<TIMETABLE_RECORDS> tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.SqlQuery(queryString, parameter).ToListAsync();

            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(tIMETABLE_RECORDS);
        }

        // PUT: api/TIMETABLE_RECORDS/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutTIMETABLE_RECORDS(decimal id, TIMETABLE_RECORDS tIMETABLE_RECORDS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != tIMETABLE_RECORDS.JOURNEY_NO)
            {
                return BadRequest();
            }

            db.Entry(tIMETABLE_RECORDS).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TIMETABLE_RECORDSExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/TIMETABLE_RECORDS
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> PostTIMETABLE_RECORDS(TIMETABLE_RECORDS tIMETABLE_RECORDS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.TIMETABLE_RECORDS.Add(tIMETABLE_RECORDS);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (TIMETABLE_RECORDSExists(tIMETABLE_RECORDS.JOURNEY_NO))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = tIMETABLE_RECORDS.JOURNEY_NO }, tIMETABLE_RECORDS);
        }

        // DELETE: api/TIMETABLE_RECORDS/5
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> DeleteTIMETABLE_RECORDS(decimal id)
        {
            TIMETABLE_RECORDS tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.FindAsync(id);
            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            db.TIMETABLE_RECORDS.Remove(tIMETABLE_RECORDS);
            await db.SaveChangesAsync();

            return Ok(tIMETABLE_RECORDS);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool TIMETABLE_RECORDSExists(decimal id)
        {
            return db.TIMETABLE_RECORDS.Count(e => e.JOURNEY_NO == id) > 0;
        }
    }
}