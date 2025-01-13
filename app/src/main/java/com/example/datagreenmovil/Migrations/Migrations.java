package com.example.datagreenmovil.Migrations;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.datagreenmovil.DataGreenApp;

public class Migrations extends RoomDatabase.Callback {


    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.i("Migration", "Running migration from version 1 to 2");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_tipos_estandar` (`id` INTEGER NOT NULL, `descripcion` TEXT, `nombreCorto` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_tipos_costo_estandar` (`id` INTEGER NOT NULL, `idNisira` INTEGER NOT NULL, `descripcion` TEXT, `nombreCorto` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_tipos_bono_estandar` (`id` INTEGER NOT NULL, `idNisira` TEXT, `descripcion` TEXT, `nombreCorto` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_medidas_estandares` (`id` INTEGER NOT NULL, `descripcion` TEXT, `nombreNISIRA` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_estandares_new` (`id` INTEGER NOT NULL, `idEmpresa` TEXT, `idTipoEstandar` TEXT, `idActividad` TEXT, `idLabor` TEXT, `periodo` TEXT, `fechaInicio` TEXT, `fechaFinal` TEXT, `idMedidaEstandar` INTEGER NOT NULL, `cantidad` REAL, `precio` REAL, `base` REAL, `precioBase` REAL, `idTipoBonoEstandar` INTEGER NOT NULL, `valMinExcedente` REAL, `horas` REAL, `idTipoCostoEstandar` INTEGER NOT NULL, `idConsumidor` TEXT, `porcentajeValidExcedente` REAL, `factorPrecio` REAL, `dniUsuarioCrea` TEXT, `fechaHoraCrea` TEXT, `dniUsuarioActualiza` TEXT, `fechaHoraActualiza` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_ParteMaquinaria` (`IdParteMaquinaria` TEXT NOT NULL, `Fecha` TEXT, `IdEmpresa` TEXT, `IdSucursal` TEXT, `IdMaquinaria` TEXT, `IdOperario` TEXT, `IdTurno` TEXT, `Observaciones` TEXT, PRIMARY KEY(`IdParteMaquinaria`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_DParteMaquinaria` (`IdParteMaquinaria` TEXT NOT NULL, `Item` TEXT, `IdConsumidor` TEXT, `TotalHoras` REAL, `HorometroInicial` REAL, `HorometroFinal` REAL, `IdImplemento` TEXT, `IdActividad` TEXT, `IdLabor` TEXT, `IdCombustible` TEXT, `Cantidad` REAL, PRIMARY KEY(`IdParteMaquinaria`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Maquinaria` (`IdMaquinaria` TEXT NOT NULL, `Descripcion` TEXT, PRIMARY KEY(`IdMaquinaria`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Implemento` (`IdImplemento` TEXT NOT NULL, `Descripcion` TEXT, PRIMARY KEY(`IdImplemento`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Operario` (`IdOperario` TEXT NOT NULL, `NombreCompleto` TEXT, `NroDocumento` TEXT, PRIMARY KEY(`IdOperario`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_consumidores_taller ` (`IdConsumidor` TEXT NOT NULL, `Descripcion` TEXT, `Tipo` TEXT, PRIMARY KEY(`IdConsumidor`))");
            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };
}
