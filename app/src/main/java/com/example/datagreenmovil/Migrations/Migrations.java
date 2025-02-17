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
//            MIGRACIÓN PARA ESTANDARES
            Log.i("Migration", "Running migration from version 1 to 2");

//            REVISIÓN
            database.execSQL("ALTER TABLE 'trx_Tareos' RENAME TO 'trx_Tareos_BK'");
            database.execSQL("ALTER TABLE 'trx_Tareos_Detalle' RENAME TO 'trx_Tareos_Detalle_BK'");
            database.execSQL("ALTER TABLE 'mst_Usuarios' RENAME TO 'mst_Usuarios_BK'");
            database.execSQL("ALTER TABLE 'mst_Estados' RENAME TO 'mst_Estados_BK'");
            database.execSQL("ALTER TABLE 'mst_Empresas' RENAME TO 'mst_Empresas_BK'");
            database.execSQL("ALTER TABLE 'mst_Turnos' RENAME TO 'mst_Turnos_BK'");
            database.execSQL("ALTER TABLE 'mst_Dias' RENAME TO 'mst_Dias_BK'");


            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_tipos_estandar` (`id` INTEGER NOT NULL, `descripcion` TEXT, `nombreCorto` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_tipos_costo_estandar` (`id` INTEGER NOT NULL, `idNisira` INTEGER NOT NULL, `descripcion` TEXT, `nombreCorto` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_tipos_bono_estandar` (`id` INTEGER NOT NULL, `idNisira` TEXT, `descripcion` TEXT, `nombreCorto` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_medidas_estandares` (`id` INTEGER NOT NULL, `descripcion` TEXT, `nombreNISIRA` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_estandares_new` (`id` INTEGER NOT NULL, `idEmpresa` TEXT, `idTipoEstandar` TEXT, `idActividad` TEXT, `idLabor` TEXT, `periodo` TEXT, `fechaInicio` TEXT, `fechaFinal` TEXT, `idMedidaEstandar` INTEGER NOT NULL, `cantidad` REAL, `precio` REAL, `base` REAL, `precioBase` REAL, `idTipoBonoEstandar` INTEGER NOT NULL, `valMinExcedente` REAL, `horas` REAL, `idTipoCostoEstandar` INTEGER NOT NULL, `idConsumidor` TEXT, `porcentajeValidExcedente` REAL, `factorPrecio` REAL, `dniUsuarioCrea` TEXT, `fechaHoraCrea` TEXT, `dniUsuarioActualiza` TEXT, `fechaHoraActualiza` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Estados` (`Id` TEXT NOT NULL, `Dex` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`Id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Dias` (`Dia` TEXT NOT NULL, `Semana` INTEGER NOT NULL, `Mes` INTEGER NOT NULL, `Anio` TEXT, `NombreDia` TEXT, `NombreDiaCorto` TEXT, `NombreMes` TEXT, `SemanaNisira` TEXT, `Periodo` TEXT, `InicioSemanaNisira` TEXT, `FinSemanaNisira` TEXT, `InicioPeriodo` TEXT, `FinPeriodo` TEXT, PRIMARY KEY(`Dia`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Empresas` (`Id` TEXT NOT NULL, `NombreCorto` TEXT, `RazonSocial` TEXT, `Ruc` TEXT, `Direccion` TEXT, `Email` TEXT, `Telefono` TEXT, `IdEstado` TEXT, `FechaHoraCreacion` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Usuarios` (`IdEmpresa` TEXT NOT NULL, `Id` TEXT NOT NULL, `NombreUsuario` TEXT, `NroDocumento` TEXT, `Nombres` TEXT, `Paterno` TEXT, `Materno` TEXT, `FechaNacimiento` TEXT, `Suma` TEXT, `Permisos` TEXT, `IdEstado` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`IdEmpresa`, `Id`))");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_mst_Usuarios_Id` ON `mst_Usuarios` (`Id`)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Turnos` (`IdEmpresa` TEXT NOT NULL, `Id` TEXT NOT NULL, `Dex` TEXT, `IdEstado` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, PRIMARY KEY(`IdEmpresa`, `Id`))");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_mst_Turnos_Id` ON `mst_Turnos` (`Id`)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_Tareos` (`IdEmpresa` TEXT, `Id` TEXT NOT NULL, `Fecha` TEXT, `IdTurno` TEXT, `IdEstado` TEXT, `IdUsuarioCrea` TEXT, `FechaHoraCreacion` TEXT, `IdUsuarioActualiza` TEXT, `FechaHoraActualizacion` TEXT, `FechaHoraTransferencia` TEXT, `TotalHoras` REAL, `TotalRendimientos` REAL, `TotalDetalles` INTEGER, `Observaciones` TEXT, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEmpresa`) REFERENCES `mst_Empresas`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`Fecha`) REFERENCES `mst_Dias`(`Dia`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdTurno`) REFERENCES `mst_Turnos`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioCrea`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_Tareos_Detalle` (`IdEmpresa` TEXT, `IdTareo` TEXT NOT NULL, `Item` INTEGER NOT NULL, `Dni` TEXT, `IdPlanilla` TEXT, `IdConsumidor` TEXT, `IdCultivo` TEXT, `IdVariedad` TEXT, `IdActividad` TEXT, `IdLabor` TEXT, `SubTotalHoras` REAL, `SubTotalRendimiento` REAL, `Observacion` TEXT, `ingreso` TEXT, `salida` TEXT, `homologar` INTEGER NOT NULL, PRIMARY KEY(`IdTareo`, `Item`));");
            database.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            database.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2a5ed5e8d0c48d6a2393c4ca3555d0e5')");

            database.execSQL("INSERT INTO trx_Tareos SELECT * FROM trx_Tareos_BK");
            database.execSQL("INSERT INTO trx_Tareos_Detalle SELECT * FROM trx_Tareos_Detalle_BK");
            database.execSQL("INSERT INTO mst_Usuarios SELECT * FROM mst_Usuarios_BK");
            database.execSQL("INSERT INTO mst_Estados SELECT * FROM mst_Estados_BK");
            database.execSQL("INSERT INTO mst_Turnos SELECT * FROM mst_Turnos_BK");
            database.execSQL("INSERT INTO mst_Empresas SELECT * FROM mst_Empresas_BK");
            database.execSQL("INSERT INTO mst_Dias SELECT * FROM mst_Dias_BK");

            database.execSQL("DROP TABLE 'trx_Tareos_BK'");
            database.execSQL("DROP TABLE 'trx_Tareos_Detalle_BK'");
            database.execSQL("DROP TABLE 'mst_Usuarios_BK'");
            database.execSQL("DROP TABLE 'mst_Estados_BK'");
            database.execSQL("DROP TABLE 'mst_Empresas_BK'");
            database.execSQL("DROP TABLE 'mst_Turnos_BK'");
            database.execSQL("DROP TABLE 'mst_Dias_BK'");

            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_IdEmpresa ON trx_Tareos(IdEmpresa);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Fecha ON trx_Tareos (Fecha);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_IdTurno ON trx_Tareos (IdTurno);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_IdEstado ON trx_Tareos (IdEstado);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_IdUsuarioCrea ON trx_Tareos (IdUsuarioCrea);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_IdUsuarioActualiza ON trx_Tareos (IdUsuarioActualiza);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_Dni ON trx_Tareos_Detalle (Dni);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_IdPlanilla ON trx_Tareos_Detalle (IdPlanilla);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_IdConsumidor ON trx_Tareos_Detalle (IdConsumidor);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_IdCultivo ON trx_Tareos_Detalle (IdCultivo);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_IdVariedad ON trx_Tareos_Detalle (IdVariedad);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_IdActividad ON trx_Tareos_Detalle(IdActividad);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_trx_Tareos_Detalle_IdLabor ON trx_Tareos_Detalle (IdLabor);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Usuarios_IdEmpresa ON mst_Usuarios (IdEmpresa);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Usuarios_NroDocumento ON mst_Usuarios (NroDocumento);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Empresas_IdEstado ON mst_Empresas (IdEstado);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Dias_Semana ON mst_Dias (Semana);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Dias_Mes ON mst_Dias (Mes);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Dias_Anio ON mst_Dias (Anio);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Turnos_IdEmpresa ON mst_Turnos (IdEmpresa);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Turnos_IdEstado ON mst_Turnos (IdEstado);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Estados_IdUsuarioCrea ON mst_Estados (IdUsuarioCrea);");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_mst_Estados_IdUsuarioActualiza ON mst_Estados (IdUsuarioActualiza);");
            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Alas` (`Id` TEXT NOT NULL, `Dex` TEXT NOT NULL, `IdEstado` TEXT NOT NULL, `IdUsuarioRegistra` TEXT NOT NULL, `Registro` TEXT NOT NULL, `IdUsuarioActualiza` TEXT NOT NULL, `Actualizacion` TEXT NOT NULL, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioRegistra`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_Lote` (`Id` TEXT NOT NULL, `Dex` TEXT NOT NULL, `IdEstado` TEXT NOT NULL, `IdUsuarioRegistra` TEXT NOT NULL, `Registro` TEXT NOT NULL, `IdUsuarioActualiza` TEXT NOT NULL, `Actualizacion` TEXT NOT NULL, PRIMARY KEY(`Id`), FOREIGN KEY(`IdUsuarioRegistra`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_ObjetivosEvaluables` (`Id` TEXT NOT NULL, `Dex` TEXT NOT NULL, `IdOrganoPlanta` TEXT, `IdEstado` TEXT NOT NULL, `IdUsuarioRegistra` TEXT NOT NULL, `Registro` TEXT NOT NULL, `IdUsuarioActualiza` TEXT NOT NULL, `Actualizacion` TEXT NOT NULL, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioRegistra`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdOrganoPlanta`) REFERENCES `mst_OrganosPlanta`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_OrganosPlanta` (`Id` TEXT NOT NULL, `Dex` TEXT NOT NULL, `IdEstado` TEXT NOT NULL, `IdUsuarioRegistra` TEXT NOT NULL, `Registro` TEXT NOT NULL, `IdUsuarioActualiza` TEXT NOT NULL, `Actualizacion` TEXT NOT NULL, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioRegistra`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `mst_TiposTrampa` (`Id` TEXT NOT NULL, `Dex` TEXT NOT NULL, `IdEstado` TEXT NOT NULL, `IdUsuarioRegistra` TEXT NOT NULL, `Registro` TEXT NOT NULL, `IdUsuarioActualiza` TEXT NOT NULL, `Actualizacion` TEXT NOT NULL, PRIMARY KEY(`Id`), FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioRegistra`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_Evaluaciones` (`Id` TEXT NOT NULL, `IdLote` TEXT, `IdTipoEvaluacion` TEXT, `Fecha` TEXT, `Linea` TEXT, `Planta` TEXT, `Cuartel` TEXT, `Inicio` TEXT, `Fin` TEXT, `Transferencia` TEXT, `IdEstado` TEXT, `IdUsuarioRegistra` TEXT, `Registro` TEXT, `IdUsuarioActualiza` TEXT, `Actualizacion` TEXT, PRIMARY KEY(`Id`), FOREIGN KEY(`IdUsuarioRegistra`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdUsuarioActualiza`) REFERENCES `mst_Usuarios`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdEstado`) REFERENCES `mst_Estados`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdLote`) REFERENCES `mst_Lote`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            database.execSQL("CREATE TABLE IF NOT EXISTS `trx_Evaluaciones_Detalle` (`IdEvaluacion` TEXT NOT NULL, `Item` INTEGER NOT NULL, `IdObjetivoEvaluable` TEXT, `IdAla` TEXT NOT NULL, `NroMuestra` INTEGER NOT NULL, `IdTipoTrampa` TEXT, PRIMARY KEY(`IdEvaluacion`, `Item`), FOREIGN KEY(`IdEvaluacion`) REFERENCES `trx_Evaluaciones`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdObjetivoEvaluable`) REFERENCES `mst_ObjetivosEvaluables`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdAla`) REFERENCES `mst_Alas`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`IdTipoTrampa`) REFERENCES `mst_TiposTrampa`(`Id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
            SharedPreferences sharedPreferences = DataGreenApp.getAppContext().getSharedPreferences("objConfLocal", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("VERSION_DB_DISPONIBLE", String.valueOf(database.getVersion())).apply();
        }
    };
//
//    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//        }
//    };
}
