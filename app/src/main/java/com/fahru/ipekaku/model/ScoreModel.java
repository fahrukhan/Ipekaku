package com.fahru.ipekaku.model;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;

public abstract class ScoreModel extends BaseModel {

    protected final String NPM_KEY = "Npm";

    protected String[] ifSem1 = {"Bahasa Indonesia", "Bahasa Inggris Umum", "Fisika Dasar", "Kalkulus", "Pemrograman Dasar",
            "Character Building", "Pengantar Teknologi Informasi", "Sistem Digital", "Algoritma & Pemrograman I"};

    protected String[] ifSem2 = {"Pemrog Khusus Platform I", "Arsitektur & Organisasi Komp", "Matematika Diskrit", "Agama",
            "Statistik & Probabilitas", "Algoritma & Pemrograman II", "Bhs Inggris TOEFL"};

    protected String[] ifSem3 = {"Rekayasa Perangkat Lunak", "Sistem Operasi", "Entrepreneurship, Innovation & Creativity",
            "Struktur Data", "pancasila", "Pemrograman WEB", "Matriks & Ruang Vektor", "Sistem Informasi"};

    protected String[] ifSem4 = {"Komunikasi Data & Jaringan Komputer", "Pemrograman Khusus Platform II", "Pemrograman Berorientasi Objek",
            "Sisten Basis Data", "Business Plan", "Kecerdasa Buatan"};

    protected String[] ifSem5 = {"Grafika Komputer", "Kualitas Pengujian Perangkat Lunak", "Pemodelan dan Simulasi",
            "Metodologi Penelitian", "Pemrograman Mobile", "Manajemen Basis Data", "Teori Bahasa Automata"};

    protected int[] ifSks1 = {2, 2, 3, 3, 3, 3, 2, 3, 3};//9
    protected int[] ifSks2 = {4, 3, 3, 2, 3, 4, 2};//7
    protected int[] ifSks3 = {3, 3, 2, 3, 2, 3, 3, 3};//8
    protected int[] ifSks4 = {3, 3, 3, 3, 3, 3};//6
    protected int[] ifSks5 = {3, 3, 3, 3, 3, 3, 3};//7


}
