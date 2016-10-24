package com.github.stonexx.slick.ext

import com.github.stonexx.scala.data.Page
import com.github.stonexx.slick.ext.ExtensionMethodConversions._
import com.github.stonexx.slick.ext.util.QueryFilter
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

trait PageableComponent { self: JdbcProfile =>
  import self.api._

  trait Paging[E, U] { paging =>
    type Filter
    type Sort
    type Result

    def query: Query[E, U, Seq]

    def onFilter: Filter => QueryFilter[E] => QueryFilter[E]

    def onSort: E => Sort => slick.lifted.Ordered

    def onResult(implicit ec: ExecutionContext): Query[E, U, Seq] => DBIOAction[Seq[Result], NoStream, Effect.All]

    def queryFilter(f: Filter): QueryFilter[E] = onFilter(f)(QueryFilter())

    def filteredQuery(f: Filter): Query[E, U, Seq] = queryFilter(f)(query)

    def pagedResult(
      page: Int, size: Int,
      countQuery: Query[E, U, Seq],
      listQuery: Query[E, U, Seq]
    )(implicit ec: ExecutionContext): DBIOAction[Page[Result], NoStream, Effect.All] = (page max 1, size max 0) match {
      case (_, 0) => for (items <- onResult(ec)(listQuery)) yield Page(items, 1, items.length, items.length)
      case (p, s) => for {
        total <- countQuery.length.result
        items <- onResult(ec)(listQuery.drop((p - 1) * s).take(s))
      } yield Page(items, p, s, total)
    }

    def count: DBIOAction[Int, NoStream, Effect.Read] = query.length.result

    def count(filter: Filter): DBIOAction[Int, NoStream, Effect.Read] = filteredQuery(filter).length.result

    def list(page: Int, size: Int)(implicit ec: ExecutionContext): DBIOAction[Page[Result], NoStream, Effect.All] =
      pagedResult(page, size, query, query)

    def list(page: Int, size: Int, sortBy: Sort)(implicit ec: ExecutionContext): DBIOAction[Page[Result], NoStream, Effect.All] =
      pagedResult(page, size, query, query.sortBy(onSort(_)(sortBy)))

    def list(page: Int, size: Int, filter: Filter)(implicit ec: ExecutionContext, $x: DummyImplicit): DBIOAction[Page[Result], NoStream, Effect.All] = {
      val fq = filteredQuery(filter)
      pagedResult(page, size, fq, fq)
    }

    def list(page: Int, size: Int, sortBy: Sort, filter: Filter)(implicit ec: ExecutionContext): DBIOAction[Page[Result], NoStream, Effect.All] = {
      val fq = filteredQuery(filter)
      val sq = fq.sortBy(onSort(_)(sortBy))
      pagedResult(page, size, fq, sq)
    }

    def list(implicit ec: ExecutionContext): DBIOAction[Seq[Result], NoStream, Effect.All] = onResult(ec)(query)

    def list(sortBy: Sort)(implicit ec: ExecutionContext): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(query.sortBy(onSort(_)(sortBy)))

    def list(filter: Filter)(implicit ec: ExecutionContext, $x: DummyImplicit): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(filteredQuery(filter))

    def list(sortBy: Sort, filter: Filter)(implicit ec: ExecutionContext): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(filteredQuery(filter).sortBy(onSort(_)(sortBy)))

    def list(size: Int)(implicit ec: ExecutionContext): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(query.takeIfPositive(size))

    def list(size: Int, sortBy: Sort)(implicit ec: ExecutionContext): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(query.sortBy(onSort(_)(sortBy)).takeIfPositive(size))

    def list(size: Int, filter: Filter)(implicit ec: ExecutionContext, $x: DummyImplicit): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(filteredQuery(filter).takeIfPositive(size))

    def list(size: Int, sortBy: Sort, filter: Filter)(implicit ec: ExecutionContext): DBIOAction[Seq[Result], NoStream, Effect.All] =
      onResult(ec)(filteredQuery(filter).sortBy(onSort(_)(sortBy)).takeIfPositive(size))

    def rawlist: DBIOAction[Seq[U], Streaming[U], Effect.Read] = query.result

    def rawlist(sortBy: Sort): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      query.sortBy(onSort(_)(sortBy)).result

    def rawlist(filter: Filter)(implicit $x: DummyImplicit): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      filteredQuery(filter).result

    def rawlist(sortBy: Sort, filter: Filter): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      filteredQuery(filter).sortBy(onSort(_)(sortBy)).result

    def rawlist(size: Int): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      query.takeIfPositive(size).result

    def rawlist(size: Int, sortBy: Sort): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      query.sortBy(onSort(_)(sortBy)).takeIfPositive(size).result

    def rawlist(size: Int, filter: Filter)(implicit $x: DummyImplicit): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      filteredQuery(filter).takeIfPositive(size).result

    def rawlist(size: Int, sortBy: Sort, filter: Filter): DBIOAction[Seq[U], Streaming[U], Effect.Read] =
      filteredQuery(filter).sortBy(onSort(_)(sortBy)).takeIfPositive(size).result

    ////////////////////////////////////////////////////////////

    def mapTo(q: Query[E, U, Seq]) = new BasicPaging[E, U](q) {
      type Filter = paging.Filter
      type Sort = paging.Sort
      type Result = paging.Result

      def onFilter: Filter => QueryFilter[E] => QueryFilter[E] = paging.onFilter
      def onSort: E => Sort => slick.lifted.Ordered = paging.onSort
      def onResult(implicit ec: ExecutionContext): Query[E, U, Seq] => DBIOAction[Seq[paging.Result], NoStream, Effect.All] = paging.onResult
    }

    def mapTo[R](f: ExecutionContext => Query[E, U, Seq] => DBIOAction[Seq[R], NoStream, Effect.All]) = new BasicPaging[E, U](query) {
      type Filter = paging.Filter
      type Sort = paging.Sort
      type Result = R

      def onFilter: Filter => QueryFilter[E] => QueryFilter[E] = paging.onFilter
      def onSort: E => Sort => slick.lifted.Ordered = paging.onSort
      def onResult(implicit ec: ExecutionContext): Query[E, U, Seq] => DBIOAction[Seq[R], NoStream, Effect.All] = f(ec)
    }

    def mapTo[R](q: Query[E, U, Seq], f: ExecutionContext => Query[E, U, Seq] => DBIOAction[Seq[R], NoStream, Effect.All]) = new BasicPaging[E, U](q) {
      type Filter = paging.Filter
      type Sort = paging.Sort
      type Result = R

      def onFilter: Filter => QueryFilter[E] => QueryFilter[E] = paging.onFilter
      def onSort: E => Sort => slick.lifted.Ordered = paging.onSort
      def onResult(implicit ec: ExecutionContext): Query[E, U, Seq] => DBIOAction[Seq[R], NoStream, Effect.All] = f(ec)
    }
  }

  abstract class BasicPaging[E, U](val query: Query[E, U, Seq]) extends Paging[E, U]

  abstract class SimplePaging[E, U](query: Query[E, U, Seq]) extends BasicPaging[E, U](query) {
    type Result = U
    def onResult(implicit ec: ExecutionContext): Query[E, U, Seq] => DBIOAction[Seq[Result], NoStream, Effect.All] = _.result
  }
}
