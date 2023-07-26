package com.ttknpdev.crudpostgresonetomany.daos;

import com.ttknpdev.crudpostgresonetomany.entities.Student;
import com.ttknpdev.crudpostgresonetomany.log.Logging;
import com.ttknpdev.crudpostgresonetomany.repositories.StudentRepository;
import com.ttknpdev.crudpostgresonetomany.repositories.SubjectRepository;
import com.ttknpdev.crudpostgresonetomany.services.StudentService;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DaoStudent extends Logging implements StudentService<Student> {

    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    @Autowired
    public DaoStudent(StudentRepository studentRepository , SubjectRepository subjectRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }
    @Override
    public List<Student> reads() {
        List<Student> response = new ArrayList<>();
        studentRepository.findAll().forEach(response::add);
        if (response.size() > 0 ) return response;
        daoStudent.log(Level.WARN,"there are no fields in students table");
        return null;
    }

    @Override
    public Student read(Long code) {
        return studentRepository.findById(code)
                .orElseThrow(()-> {
                    daoStudent.log(Level.DEBUG,"there are no code "+code+" in students table");
                    throw new RuntimeException("there are no code "+code+" in students table");
                });
    }

    @Override
    public Student create(Student obj) {
        return studentRepository.save(obj);
    }

    @Override
    public Map<String, Student> update(Student obj , Long code) {
        Map<String , Student> response = new HashMap<>();
        return studentRepository.findById(code)
                .map(student -> {
                    student.setFullname(obj.getFullname());
                    student.setAge(obj.getAge());
                    student.setStatus(obj.getStatus());
                    student.setWeight(obj.getWeight());
                    student.setHeight(obj.getHeight());
                    studentRepository.save(student);
                    response.put("updated" , student);
                    return  response;
                })
                .orElseThrow(()->{
                    daoStudent.log(Level.DEBUG,"there are no code "+code+" in students table");
                    throw new RuntimeException("there are no code "+code+" in students table");
                });
    }

    @Override

    public Map<String, Student> delete(Long code) {
        Map<String , Student> response = new HashMap<>();
        return studentRepository.findById(code)
                .map(student -> {
                    subjectRepository.deleteByForeignKey(code);
                    studentRepository.delete(student);
                    response.put("deleted",student);
                    return response;
                }).orElseThrow(()->{
                    daoStudent.log(Level.DEBUG,"there are no code "+code+" in students table");
                    throw new RuntimeException("there are no code "+code+" in students table");
                });
    }
}
