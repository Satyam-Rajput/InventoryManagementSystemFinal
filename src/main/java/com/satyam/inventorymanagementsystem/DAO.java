/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.satyam.inventorymanagementsystem;

import java.nio.file.Paths;
import java.util.List;
import javafx.scene.control.Alert;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Satyam This file is created for database operations purpose. This
 * file has all the methods needed to perform DB operations for User & Item
 * classes
 */
public class DAO {

    //Creating a static Session Factory reference variable so that it will create only once for a single DAO Object
    private static SessionFactory sessionFactory = null;

    //instantiating the static session factory reference variable with Annotated classes
    public DAO() {

        try {
            sessionFactory = new Configuration().
                    configure("com/satyam/inventorymanagementsystem/hibernate.cfg.xml").
                    buildSessionFactory();
        } catch (Exception ex) {
            //this will show an alert pop up to the user is we get any exceptions while creating session factory object
           
            System.out.println(ex);
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Cannot connect to Database");

        } finally {
            this.addAdmin();
        }

    }

    private void addAdmin() {
        User user = new User();
        user.setAccessType("Admin");
        user.setPassword(user.encode("admin@"));
        user.setUsername("Admin");
        try ( Session session = sessionFactory.openSession()) {
            //session=sessionFactory.openSession();
            if (((Object) session.get(User.class, user.getUsername()) == null)) {
                Transaction transaction = session.beginTransaction();
                session.saveOrUpdate(user);
                transaction.commit();

            }

        } catch (Exception e) {

//            return false;
        }

    }

    //Following methods are related to Item Class only
    public boolean addNewItem(Item item) {

        try ( Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(item);
            transaction.commit();
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public List<String> getMedicineNames() {
        try ( Session session = sessionFactory.openSession()) {
            Query q = session.createQuery("select name from Item where quantity>0 order by name");
            return q.list();
        } catch (Exception e) {
            return null;
        }
    }

    public Double getPrice(String name) {
        try ( Session session = sessionFactory.openSession()) {
            Query q = session.createQuery("select price from Item where name='" + name.toUpperCase() + "'");
            return (Double) q.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getQuantity(String name) {
        try ( Session session = sessionFactory.openSession()) {
            Query q = session.createQuery("select quantity from Item where name='" + (name.toUpperCase()) + "'");
            return (Integer) q.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Item getItem(String name) {

        try ( Session session = sessionFactory.openSession()) {
            return session.get(Item.class, name);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Item> getAllItems() {
        try ( Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Item order by name");
            return query.list();
        } catch (Exception e) {
            return null;
        }
    }

    //Following methods are related to User Class only
    public boolean addNewUser(User user) {

        try ( Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public User getUser(String name) {

        try ( Session session = sessionFactory.openSession()) {
            return session.get(User.class, name);
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        try ( Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from User");
            return query.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> getAllPatients() {
        try ( Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Patient");
            return query.list();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean addPatient(Patient patient) {
        try ( Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(patient);
            transaction.commit();
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public List<Patient> getPatient(String name, String date) {
        try ( Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Patient where name='" + name + "' and date='" + date + "'");
            return query.list();
        } catch (Exception e) {
            return null;
        }

    }
    
    public boolean deletePatients(String date)
    {
    try ( Session session = sessionFactory.openSession()) {
        Transaction t=session.beginTransaction();
            Query query = session.createQuery("from Patient where date <'" + date + "'");
           
            List<Patient> list=query.list();
            
            for(Patient p:list)
            {
                session.remove(p);
            }
            
            
           t.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Below code if for Testing of DAO
    public static void main(String[] args) {
    
  //DAO d = new DAO();
  
  String path=System.getProperty("user.home")+"/Desktop";
  System.out.println(path);
  
  
//  List<String> list=d.getMedicineNames();
//  for(String i:list)
//  {
//  System.out.println(i);
//  }
       
    }}
//        d.deletePatients("2022-02-28");


//Session session=sessionFactory.openSession();
//Query query=session.createNativeQuery("create database hello");
//query.executeUpdate();
        

    
//
//        
//        List<Patient> p=d.getPatient("g","2022-02-28");
//        p=p.stream().filter(
//                x->x.getName().equals("g")
//        ).filter(y->y.getDate().equals("2022-02-28")).collect(Collectors.toList());
//        
//        for(Patient x:p)
//        {
//            System.out.println(x.getList());
//        
//        }
////        for(Patient s:p)
//        {
//            System.out.println(s.getName()+" "+s.getDate());
//        }
//         System.out.println(d.getPatient("g", "2022-02-28").get(0).getList());
//        System.out.println(d.getPatient("g", "2022-02-28"));
//        
//        Session session = sessionFactory.openSession();
//        Patient p=session.get(Patient.class, 4);
//        System.out.println(p.getName()+" "+p.getDate()+" "+p.getTotal());
//        List<Medicine> list=p.getList();
//        for(Medicine m:list)
//        {
//            System.out.println(m.getName()+" "+m.getPrice()+" "+m.getQuantity());
//        
//        }
//
//        
//        User u=new User();
//        u.setAccessType("Admin");
//        u.setUsername("Satyam");
//        u.setPassword(u.encode("123"));
//        d.addNewUser(u);
//        System.out.println(u.getPassword());
//        Medicine m1=new Medicine();
//        m1.setName("Dolo");
//        m1.setPrice(10);
//        m1.setQuantity(50);
//        Medicine m2=new Medicine();
//        m2.setName("Dolo1");
//        m2.setPrice(5);
//        m2.setQuantity(10);
//        List <Medicine> list=new ArrayList<>();
//        list.add(m2);
//        list.add(m1);
//        double total=0;
//        for(Medicine m:list)
//        {
//            total+=m.calculateTotal();
//        }
//        Patient p=new Patient();
//        p.setName("Satyam");
//        p.setDate("2022-03-27");
//        p.setList(list);
//        p.setTotal(total);
//       
//        d.addPatient(p);
//        List<String> l=d.getMedicineNames();
//        for(String s:l)
//        {
//        System.out.println(s);}
//        
    // User u = new User("Admin", "Admin", "123", new Date());
    //  d.addNewUser(u);
//        Item item1=new Item("DOLO",20,50.0);
//        Item item2=new Item("DOLO1",20,50.0);
//        Item item3=new Item("DOLO2",20,50.0);
//        Item item4=new Item("DOLO4",20,50.0);
//        
//        
//        if(d.addNewItem(item1)&&d.addNewItem(item2)&&d.addNewItem(item3)&&d.addNewItem(item4))
//        {
//            System.err.println("Objects created");
//        }
//        item1=d.getItem("DOLO");
//        if(item1!=null)
//        {System.err.println(item1.getName()+" "+item1.getQuantity()+" "+item1.getPrice());
//        }
//        
//        List<Item> items=d.getAllItems();
//        
//        for(Item item:items)
//        {System.err.println(item.getName()+" "+item.getQuantity()+" "+item.getPrice());
//        }
//        System.out.println("Hello");
//    }
//}
