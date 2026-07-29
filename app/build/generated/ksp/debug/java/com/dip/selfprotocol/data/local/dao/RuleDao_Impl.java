package com.dip.selfprotocol.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dip.selfprotocol.data.local.entity.RuleEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RuleDao_Impl implements RuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RuleEntity> __insertionAdapterOfRuleEntity;

  private final EntityDeletionOrUpdateAdapter<RuleEntity> __deletionAdapterOfRuleEntity;

  private final EntityDeletionOrUpdateAdapter<RuleEntity> __updateAdapterOfRuleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRules;

  public RuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRuleEntity = new EntityInsertionAdapter<RuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rules` (`id`,`categoryId`,`question`,`brutalAnswer`,`rule`,`createdAt`,`lastEditedAt`,`isFavorite`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RuleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindString(3, entity.getQuestion());
        statement.bindString(4, entity.getBrutalAnswer());
        statement.bindString(5, entity.getRule());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getLastEditedAt());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__deletionAdapterOfRuleEntity = new EntityDeletionOrUpdateAdapter<RuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RuleEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRuleEntity = new EntityDeletionOrUpdateAdapter<RuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rules` SET `id` = ?,`categoryId` = ?,`question` = ?,`brutalAnswer` = ?,`rule` = ?,`createdAt` = ?,`lastEditedAt` = ?,`isFavorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RuleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindString(3, entity.getQuestion());
        statement.bindString(4, entity.getBrutalAnswer());
        statement.bindString(5, entity.getRule());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getLastEditedAt());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllRules = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rules";
        return _query;
      }
    };
  }

  @Override
  public Object insertRule(final RuleEntity rule, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRuleEntity.insert(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertRules(final List<RuleEntity> rules,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRuleEntity.insert(rules);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRule(final RuleEntity rule, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRuleEntity.handle(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRules(final List<RuleEntity> rules,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRuleEntity.handleMultiple(rules);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRule(final RuleEntity rule, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRuleEntity.handle(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllRules(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRules.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllRules.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RuleEntity>> getRulesByCategory(final int categoryId) {
    final String _sql = "SELECT * FROM rules WHERE categoryId = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rules"}, new Callable<List<RuleEntity>>() {
      @Override
      @NonNull
      public List<RuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfBrutalAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "brutalAnswer");
          final int _cursorIndexOfRule = CursorUtil.getColumnIndexOrThrow(_cursor, "rule");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEditedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<RuleEntity> _result = new ArrayList<RuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RuleEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpBrutalAnswer;
            _tmpBrutalAnswer = _cursor.getString(_cursorIndexOfBrutalAnswer);
            final String _tmpRule;
            _tmpRule = _cursor.getString(_cursorIndexOfRule);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastEditedAt;
            _tmpLastEditedAt = _cursor.getLong(_cursorIndexOfLastEditedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item = new RuleEntity(_tmpId,_tmpCategoryId,_tmpQuestion,_tmpBrutalAnswer,_tmpRule,_tmpCreatedAt,_tmpLastEditedAt,_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RuleEntity>> searchRules(final String query) {
    final String _sql = "SELECT * FROM rules WHERE question LIKE '%' || ? || '%' OR brutalAnswer LIKE '%' || ? || '%' OR rule LIKE '%' || ? || '%' ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rules"}, new Callable<List<RuleEntity>>() {
      @Override
      @NonNull
      public List<RuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfBrutalAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "brutalAnswer");
          final int _cursorIndexOfRule = CursorUtil.getColumnIndexOrThrow(_cursor, "rule");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEditedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<RuleEntity> _result = new ArrayList<RuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RuleEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpBrutalAnswer;
            _tmpBrutalAnswer = _cursor.getString(_cursorIndexOfBrutalAnswer);
            final String _tmpRule;
            _tmpRule = _cursor.getString(_cursorIndexOfRule);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastEditedAt;
            _tmpLastEditedAt = _cursor.getLong(_cursorIndexOfLastEditedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item = new RuleEntity(_tmpId,_tmpCategoryId,_tmpQuestion,_tmpBrutalAnswer,_tmpRule,_tmpCreatedAt,_tmpLastEditedAt,_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllRulesSync(final Continuation<? super List<RuleEntity>> $completion) {
    final String _sql = "SELECT * FROM rules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RuleEntity>>() {
      @Override
      @NonNull
      public List<RuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfBrutalAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "brutalAnswer");
          final int _cursorIndexOfRule = CursorUtil.getColumnIndexOrThrow(_cursor, "rule");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEditedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<RuleEntity> _result = new ArrayList<RuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RuleEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpBrutalAnswer;
            _tmpBrutalAnswer = _cursor.getString(_cursorIndexOfBrutalAnswer);
            final String _tmpRule;
            _tmpRule = _cursor.getString(_cursorIndexOfRule);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastEditedAt;
            _tmpLastEditedAt = _cursor.getLong(_cursorIndexOfLastEditedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item = new RuleEntity(_tmpId,_tmpCategoryId,_tmpQuestion,_tmpBrutalAnswer,_tmpRule,_tmpCreatedAt,_tmpLastEditedAt,_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
