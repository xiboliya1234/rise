
package com.rise.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

	  
	  
	  
	  @Select(value = { "SELECT count(id) FROM user where username =#{username} and password =#{password}" })
	  int getuser(@Param("username")String username,@Param("password")String password);
	  
	  
	  @Select(value = { "SELECT count(*) FROM sf4 " })
	  int getmapcount();
	  
	  @Select(value = { "SELECT count(id) FROM user where username =#{username} and password =#{password}" })
	  int getuser(@Param("username")String username);
	  
	  @Select(value = { "SELECT sbmc FROM sf4 where x is  null or y is  null" })
	  String[] getmapcoun1t();
	  
	  @Select(value = { "SELECT concat(x1,',',y1) FROM sf4 where lx ='高速门架' or lx ='基站数据'" })
	  String[] getx1y1();
	  
	  
	  @Update(value = { "update  user set status =1 where username =#{username} and password =#{password}" })
	  void updateuserstatus(@Param("username")String username,@Param("password")String password);
	  
	  
	  @Update(value = { "update  sf4 set x1 =#{x} ,y1=#{y} where sbmc =#{sbmc}"})
	  void updatesf4(@Param("x")String x,@Param("y")String y,@Param("sbmc")String sbmc);
	  
	  
	  
	  @Update(value = { "update  sf4 set s =#{s} ,q=#{q} ,jd=#{jd},xz=#{xz}where sbmc =#{sbmc}"})
	  void updatesf41(@Param("s")String s,@Param("q")String q,@Param("jd")String jd,@Param("xz")String xz,@Param("sbmc")String sbmc);
	  
	  
	  
	  @Select(value = { "SELECT sbmc FROM sf4 where lx ='高速门架' or lx ='基站数据' and s is null" })
	  String[] getgsmj();
	  
	  @Select(value = { "SELECT concat(x,',',y) FROM sf4 where   sbmc =#{sbmc} limit 1" })
	  String getx1y2(@Param("sbmc")String sbmc);
}
