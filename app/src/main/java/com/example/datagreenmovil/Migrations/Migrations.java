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
            database.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'trx_Tareos' RENAME TO 'trx_Tareos_BK'");
            database.execSQL("ALTER TABLE 'trx_Tareos_Detalle' RENAME TO 'trx_Tareos_Detalle_BK'");
            database.execSQL("ALTER TABLE 'mst_Usuarios' RENAME TO 'mst_Usuarios_BK'");
            database.execSQL("ALTER TABLE 'mst_Estados' RENAME TO 'mst_Estados_BK'");
            database.execSQL("ALTER TABLE 'mst_Empresas' RENAME TO 'mst_Empresas_BK'");
            database.execSQL("ALTER TABLE 'mst_Turnos' RENAME TO 'mst_Turnos_BK'");
            database.execSQL("ALTER TABLE 'mst_Dias' RENAME TO 'mst_Dias_BK'");
//            CREACIÓN
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Estados` (`Id` TEXT NOT NULL, `Dex` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`Id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Dias` (`Dia` TEXT NOT NULL, `Semana` INTEGER NOT NULL, `Mes` INTEGER NOT NULL, `Anio` TEXT, `NombreDia` TEXT, `NombreDiaCorto` TEXT, `NombreMes` TEXT, `SemanaNisira` TEXT, `Periodo` TEXT, `InicioSemanaNisira` TEXT, `FinSemanaNisira` TEXT, `InicioPeriodo` TEXT, `FinPeriodo` TEXT, PRIMARY KEY(`Dia`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Empresas` (`Id` TEXT NOT NULL, `NombreCorto` TEXT, `RazonSocial` TEXT, `Ruc` TEXT, `Direccion` TEXT, `Email` TEXT, `Telefono` TEXT, `IdEstado` TEXT, `FechaHoraCreacion` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Usuarios` (`IdEmpresa` TEXT NOT NULL, `Id` TEXT NOT NULL, `NombreUsuario` TEXT, `NroDocumento` TEXT, `Nombres` TEXT, `Paterno` TEXT, `Materno` TEXT, `FechaNacimiento` TEXT, `Suma` TEXT, `Permisos` TEXT, `IdEstado` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`IdEmpresa`, `Id`))");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_mst_Usuarios_Id` ON `mst_Usuarios` (`Id`)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Turnos` (`IdEmpresa` TEXT NOT NULL, `Id` TEXT NOT NULL, `Dex` TEXT, `IdEstado` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`IdEmpresa`, `Id`))");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_mst_Turnos_Id` ON `mst_Turnos` (`Id`)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_Tareos` (`Id` TEXT NOT NULL, `IdEmpresa` TEXT, `Fecha` TEXT, `IdTurno` TEXT, `IdEstado` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, `FechaHoraTransferencia` TEXT, `TotalHoras` REAL, `TotalRendimientos` REAL, `TotalDetalles` INTEGER, `Observaciones` TEXT, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEmpresa`) REFERENCES `mst_Empresas`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`Fecha`) REFERENCES `mst_Dias`(`Dia`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdTurno`) REFERENCES `mst_Turnos`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioCrea`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_Tareos_Detalle` (`idEmpresa` TEXT, `idTareo` TEXT NOT NULL, `item` INTEGER NOT NULL, `dni` TEXT, `idPlanilla` TEXT, `idConsumidor` TEXT, `idCultivo` TEXT, `idVariedad` TEXT, `idActividad` TEXT, `idLabor` TEXT, `subTotalHoras` REAL, `subTotalRendimiento` REAL, `observacion` TEXT, `ingreso` TEXT, `salida` TEXT, `homologar` INTEGER NOT NULL, PRIMARY KEY(`idTareo`, `item`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            database.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'df906f57f2fd9dd98f84f8334166143a')");

            database.execSQL("INSERT INTO trx_Tareos SELECT * FROM trx_Tareos_BK");
            database.execSQL("INSERT INTO trx_Tareos_Detalle SELECT * FROM trx_Tareos_Detalle_BK");
            database.execSQL("INSERT INTO mst_Usuarios SELECT * FROM mst_Usuarios_BK");
            database.execSQL("INSERT INTO mst_Estados SELECT * FROM mst_Estados_BK");
            database.execSQL("INSERT INTO mst_Turnos SELECT * FROM mst_Turnos_BK");
            database.execSQL("INSERT INTO mst_Empresas SELECT * FROM mst_Empresas_BK");
            database.execSQL("INSERT INTO mst_Dias SELECT * FROM mst_Dias_BK");

            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE 'trx_Tareos_BK'");
            database.execSQL("DROP TABLE 'trx_Tareos_Detalle_BK'");
            database.execSQL("DROP TABLE 'mst_Usuarios_BK'");
            database.execSQL("DROP TABLE 'mst_Estados_BK'");
            database.execSQL("DROP TABLE 'mst_Empresas_BK'");
            database.execSQL("DROP TABLE 'mst_Turnos_BK'");
            database.execSQL("DROP TABLE 'mst_Dias_BK'");
        }
    };
}
