using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using WebApplication1.Models;
using System.Web.Http;

namespace WebApplication1.Controllers
{
    public class CUSTOMERsController : Controller
    {
        private Entities db = new Entities();

        // GET: CUSTOMERs
        public ActionResult Index()
        {
            return View(db.CUSTOMERS.ToList());
        }

        // GET: CUSTOMERs/Details/5
        public ActionResult Details(decimal id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            CUSTOMER cUSTOMER = db.CUSTOMERS.Find(id);
            if (cUSTOMER == null)
            {
                return HttpNotFound();
            }
            return View(cUSTOMER);
        }

        // GET: CUSTOMERs/Create
        public ActionResult Create()
        {
            return View();
        }

        [System.Web.Mvc.HttpPost]
        //[ValidateAntiForgeryToken]
        public ActionResult Login([FromBody]CUSTOMER objUser)
        {
            if (ModelState.IsValid)
            {
                using (Entities db = new Entities())
                {
                    var obj = db.CUSTOMERS.Where(a => a.USERNAME.Equals(objUser.USERNAME) && a.PASSWORD.Equals(objUser.PASSWORD)).FirstOrDefault();
                    if (obj != null)
                    {
                        Response.StatusCode = 200;
                        
                    }
                    else
                    {
                        
                        Response.StatusCode = 404;
                    }

                }
            }
            return View(objUser);
        }

        // POST: CUSTOMERs/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [System.Web.Mvc.HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "CUSTOMER_ID,CUSTOMER_FORENAME,CUSTOMER_SURNAME,USERNAME,PASSWORD")] CUSTOMER cUSTOMER)
        {
            if (ModelState.IsValid)
            {
                db.CUSTOMERS.Add(cUSTOMER);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(cUSTOMER);
        }

        // GET: CUSTOMERs/Edit/5
        public ActionResult Edit(decimal id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            CUSTOMER cUSTOMER = db.CUSTOMERS.Find(id);
            if (cUSTOMER == null)
            {
                return HttpNotFound();
            }
            return View(cUSTOMER);
        }

        // POST: CUSTOMERs/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [System.Web.Mvc.HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "CUSTOMER_ID,CUSTOMER_FORENAME,CUSTOMER_SURNAME,USERNAME,PASSWORD")] CUSTOMER cUSTOMER)
        {
            if (ModelState.IsValid)
            {
                db.Entry(cUSTOMER).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(cUSTOMER);
        }

        // GET: CUSTOMERs/Delete/5
        public ActionResult Delete(decimal id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            CUSTOMER cUSTOMER = db.CUSTOMERS.Find(id);
            if (cUSTOMER == null)
            {
                return HttpNotFound();
            }
            return View(cUSTOMER);
        }

        // POST: CUSTOMERs/Delete/5
        [System.Web.Mvc.HttpPost, System.Web.Mvc.ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(decimal id)
        {
            CUSTOMER cUSTOMER = db.CUSTOMERS.Find(id);
            db.CUSTOMERS.Remove(cUSTOMER);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
